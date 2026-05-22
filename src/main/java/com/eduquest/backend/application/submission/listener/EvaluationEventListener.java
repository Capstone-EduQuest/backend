package com.eduquest.backend.application.submission.listener;

import com.eduquest.backend.domain.submission.event.SubmissionEvaluatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 채점 완료 이벤트 수신 후 후속 처리(로깅 등)를 담당하는 리스너.
 * Evaluation 저장은 PistonEvaluationWorkerService에서 직접 처리하므로 여기서는 수행하지 않는다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EvaluationEventListener {

    @Async("virtualThreadTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSubmissionEvaluatedEvent(SubmissionEvaluatedEvent event) {
        log.info("SubmissionEvaluatedEvent received: submissionId={}, isCorrect={}", event.submissionId(), event.isCorrect());
    }
}
