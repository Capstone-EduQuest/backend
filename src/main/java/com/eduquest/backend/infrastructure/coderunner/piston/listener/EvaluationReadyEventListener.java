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

    @Async("virtualThreadTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEvaluationReadyEvent(EvaluationReadyEvent event) {
        UUID submissionUuid = event.submissionUuid();

        boolean offered = evaluationQueueRepository.offer(submissionUuid);
        if (!offered) {
            log.warn("Evaluation queue offer failed for submissionUuid={}", submissionUuid);
        }
    }
}
