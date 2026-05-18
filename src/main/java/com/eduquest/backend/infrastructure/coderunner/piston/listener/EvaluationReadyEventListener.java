package com.eduquest.backend.infrastructure.coderunner.piston.listener;

import com.eduquest.backend.domain.submission.event.EvaluationReadyEvent;
import com.eduquest.backend.infrastructure.coderunner.repository.EvaluationQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class EvaluationReadyEventListener {

    private final EvaluationQueueRepository evaluationQueueRepository;

    @Async("coderunnerTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEvaluationReadyEvent(EvaluationReadyEvent event) {
        UUID submissionUuid = event.submissionUuid();

        boolean offered = evaluationQueueRepository.offer(submissionUuid);

        if (!offered) {
            log.warn("Evaluation queue offer failed for submissionUuid={}", submissionUuid);


        }
    }

    /*@Async("coderunnerTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEvaluationReadyEvent(EvaluationReadyEvent event) {

        UUID submissionUuid = event.submissionUuid();

        // 1) 큐에 등록
        boolean offered = evaluationQueueRepository.offer(submissionUuid);
        if (!offered) {
            log.warn("Evaluation queue offer failed for submissionUuid={}", submissionUuid);
        }

        try {
            // 2) 큐에서 꺼내 처리
            UUID uuidToProcess = evaluationQueueRepository.take();

            Submission submission = submissionQueryService.findSubmissionByUuid(uuidToProcess);

            Long submissionId = submission.getId();

            // 3) 문제 정보를 가져와서 채점 혹은 비교 처리
            Problem problem = problemQueryService.findProblemById(submission.getProblemId());

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

            // 4) 결과 저장
            evaluationCommandService.saveEvaluation(isCorrect, submissionId);

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            log.error("Failed to evaluate submissionUuid={}", submissionUuid, ex);
            evaluationQueueRepository.offer(submissionUuid);
        }
    }*/


}

