package com.eduquest.backend.infrastructure.coderunner.piston.listener;

import com.eduquest.backend.domain.learning.model.Problem;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.domain.submission.dto.request.CodeEvaluateRequest;
import com.eduquest.backend.domain.submission.dto.response.CodeEvaluateResponse;
import com.eduquest.backend.domain.submission.event.EvaluationReadyEvent;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.service.CodeRunnerService;
import com.eduquest.backend.domain.submission.service.EvaluationCommandService;
import com.eduquest.backend.domain.submission.service.SubmissionQueryService;
import com.eduquest.backend.infrastructure.coderunner.repository.EvaluationQueueRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class EvaluationReadyEventListener {

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

    private final EvaluationQueueRepository evaluationQueueRepository;
    private final CodeRunnerService codeRunnerService;
    private final EvaluationCommandService evaluationCommandService;
    private final SubmissionQueryService submissionQueryService;
    private final ProblemQueryService problemQueryService;
    private final ObjectMapper objectMapper;


    @Async("coderunnerTaskExecutor")
    @EventListener
    public void onEvaluationReady(EvaluationReadyEvent event) {
        UUID submissionUuid = event.submissionUuid();

        // 1) 큐에 등록
        boolean offered = evaluationQueueRepository.offer(submissionUuid);
        if (!offered) {
            log.warn("Evaluation queue offer failed for submissionUuid={}", submissionUuid);
        }

        try {
            // 2) 큐에서 꺼내 처리
            UUID uuidToProcess = evaluationQueueRepository.take();

            Submission submission = submissionQueryService.findByUuid(uuidToProcess);
            if (submission == null) {
                // 제출 레코드가 존재하지 않음
                return;
            }

            Long submissionId = submission.getId();

            // 3) 문제 정보를 가져와서 채점 혹은 비교 처리
            Problem problem = problemQueryService.findProblemById(submission.getProblemId());

            boolean isCorrect;
            if ("basic".equalsIgnoreCase(Objects.toString(problem.getType(), ""))) {
                // 기본 문제는 정답 문자열 비교
                isCorrect = compareAnswers(problem.getExpectedOutput(), submission.getAnswer());
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

            // 4) 결과 저장
            evaluationCommandService.saveEvaluation(isCorrect, submissionId);

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            // TODO: 재시도/오류 처리
            log.error("Failed to evaluate submissionUuid={}", submissionUuid, ex);
        }
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

    private String normalizeNewlines(String s) {
        return s == null ? null : s.replace("\r\n", "\n");
    }
}

