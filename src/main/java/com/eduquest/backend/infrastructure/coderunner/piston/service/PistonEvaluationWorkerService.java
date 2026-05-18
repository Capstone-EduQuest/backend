package com.eduquest.backend.infrastructure.coderunner.piston.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.learning.model.Problem;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.domain.submission.dto.request.CodeEvaluateRequest;
import com.eduquest.backend.domain.submission.dto.response.CodeEvaluateResponse;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.model.enums.SubmissionStatus;
import com.eduquest.backend.domain.submission.service.*;
import com.eduquest.backend.infrastructure.coderunner.exception.CodeRunnerErrorCode;
import com.eduquest.backend.infrastructure.coderunner.repository.EvaluationQueueRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PistonEvaluationWorkerService implements EvaluationWorkerService {

    @Value("${coderunner.config.language.python.version}")
    private String languageVersion;
    @Value("${coderunner.config.language.python.file}")
    private String fileName;
    @Value("${coderunner.config.limit.compilation.time}")
    private String compileTimeLimitMs;
    @Value("${coderunner.config.limit.compilation.memory}")
    private String compileTimeMemoryLimitKb;
    @Value("${coderunner.config.limit.runtime.time}")
    private String runTimeLimitMs;
    @Value("${coderunner.config.limit.runtime.memory}")
    private String runtTimeMemoryLimitKb;

    private static final String DEFAULT_LANGUAGE = "python";
    private static final int LOG_TRUNCATE_MAX = 2000;
    private static final int retryLimit = 3;

    private final EvaluationQueueRepository evaluationQueueRepository;
    private final CodeRunnerService codeRunnerService;
    private final EvaluationCommandService evaluationCommandService;
    private final SubmissionQueryService submissionQueryService;
    private final ProblemQueryService problemQueryService;
    private final SubmissionCommandService submissionCommandService;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public void processSingle() {

        UUID uuidToProcess = null;
        Long submissionId = null;

        try {
            // 1) 큐에서 꺼내 처리
            uuidToProcess = evaluationQueueRepository.take();

            Submission submission = submissionQueryService.findSubmissionByUuid(uuidToProcess);
            submissionCommandService.updateStatus(submission.getId(), SubmissionStatus.PROCESSING);

            submissionId = submission.getId();

            // 2) 문제 정보를 가져와서 채점 혹은 비교 처리
            Problem problem = problemQueryService.findProblemById(submission.getProblemId());

            boolean isCorrect = processEvaluation(problem, submission);

            // 3) 결과 저장
            evaluationCommandService.saveEvaluation(isCorrect, submissionId);
        } catch (EduQuestException e) {

            if (e.getErrorCode() == CodeRunnerErrorCode.CODE_RUNNER_CLIENT_ERROR) {
                // 실패
                submissionCommandService.updateStatus(submissionId, SubmissionStatus.FAILED);
            } else if (e.getErrorCode() == CodeRunnerErrorCode.CODE_RUNNER_SERVER_ERROR) {
                // 재시도
                submissionCommandService.updateStatus(submissionId, SubmissionStatus.RETRYING);
                processWithRetry(uuidToProcess);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void processWithRetry(UUID submissionUuid) {

        Submission submission = submissionQueryService.findSubmissionByUuid(submissionUuid);
        Long submissionId = submission.getId();
        Problem problem = problemQueryService.findProblemById(submission.getProblemId());

        int retryCount = 0;

        while (retryCount < retryLimit) {

            retryCount++;
            submissionCommandService.updateRetryCount(submissionId);

            try {

                boolean isCorrect = processEvaluation(problem, submission);
                evaluationCommandService.saveEvaluation(isCorrect, submissionId);
                submissionCommandService.updateStatus(submissionId, SubmissionStatus.SUCCEEDED);
                return;

            } catch (EduQuestException exception) {

                if (exception.getErrorCode() == CodeRunnerErrorCode.CODE_RUNNER_CLIENT_ERROR) {
                    submissionCommandService.updateStatus(submissionId, SubmissionStatus.FAILED);
                    return;
                }

                if (retryCount >= retryLimit) {
                    submissionCommandService.updateStatus(submissionId, SubmissionStatus.FAILED);
                    return;
                }

            } catch (Exception exception) {
                submissionCommandService.updateStatus(submissionId, SubmissionStatus.FAILED);
                return;
            }
        }

    }

    private boolean processEvaluation(Problem problem, Submission submission) {

        boolean isCorrect;

        if ("basic".equalsIgnoreCase(Objects.toString(problem.getType(), ""))) {
            // 기본 문제는 정답 문자열 비교
            isCorrect = compareAnswers(extractBasicProblemAnswer(problem.getBlock()), submission.getAnswer());
        } else {
            // 코드 문제: CodeRunnerService 사용
            String source = submission.getAnswer();
            CodeEvaluateRequest request = CodeEvaluateRequest.of(
                    source,
                    DEFAULT_LANGUAGE,
                    languageVersion,
                    fileName,
                    "",
                    Long.parseLong(compileTimeLimitMs),
                    Long.parseLong(compileTimeMemoryLimitKb),
                    Long.parseLong(runTimeLimitMs),
                    Long.parseLong(runtTimeMemoryLimitKb),
                    false
            );

            CodeEvaluateResponse evaluateResponse = codeRunnerService.evaluate(request);
            if (evaluateResponse != null) {
                String stdout = evaluateResponse.stdout() == null ? "" : evaluateResponse.stdout();
                isCorrect = compareAnswers(problem.getExpectedOutput(), stdout);
            } else {
                isCorrect = false;
            }
        }

        return isCorrect;

    }

    private boolean compareAnswers(String expected, String answer) {

        if (expected == null)
            return false;
        if (answer == null)
            return false;

        String expNorm = normalizeNewlines(expected).trim();
        String ansNorm = normalizeNewlines(answer).trim();

        try {
            JsonNode expectedNode = objectMapper.readTree(expNorm);
            JsonNode answerNode = objectMapper.readTree(ansNorm);
            return expectedNode.equals(answerNode);
        } catch (Exception e) {
            return expNorm.equals(ansNorm);
        }
    }

    private String extractBasicProblemAnswer(String block) {

        try {
            JsonNode blockJson = objectMapper.readTree(block);

            return String.valueOf(blockJson.get("answer"));

        } catch (JsonProcessingException e) {
            throw new RuntimeException("블록 JSON 파싱 실패", e);
        }

    }

    private String normalizeNewlines(String s) {
        return s == null ? null : s.replace("\r\n", "\n");
    }
}
