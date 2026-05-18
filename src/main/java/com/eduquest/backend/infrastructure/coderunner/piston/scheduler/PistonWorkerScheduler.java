package com.eduquest.backend.infrastructure.coderunner.piston.scheduler;

import com.eduquest.backend.domain.submission.service.EvaluationWorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PistonWorkerScheduler {

    private final EvaluationWorkerService evaluationWorkerService;

    @Scheduled(fixedDelay = 100)
    public void pollAndProcess() {
        evaluationWorkerService.processSingle();
    }
}
