package com.eduquest.backend.application.submission.listener;

import com.eduquest.backend.domain.submission.event.SubmissionEvaluatedEvent;
import com.eduquest.backend.domain.submission.service.EvaluationCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvaluationEventListener {

    private final EvaluationCommandService evaluationCommandService;

    @Async("evaluationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSubmissionEvaluatedEvent(SubmissionEvaluatedEvent event) {
        try {
            Long id = evaluationCommandService.saveEvaluation(event.isCorrect(), event.submissionId());
            log.info("Evaluation saved asynchronously (after commit): id={}, submissionId={}, isCorrect={}", id, event.submissionId(), event.isCorrect());
        } catch (Exception ex) {
            log.error("Failed to save Evaluation for submissionId={} asynchronously: {}", event.submissionId(), ex.getMessage(), ex);
        }
    }
}

