package com.eduquest.backend.application.learning.listener;

import com.eduquest.backend.domain.learning.event.UseHintEvent;
import com.eduquest.backend.domain.learning.model.HintHistory;
import com.eduquest.backend.domain.learning.service.HintHistoryCommandService;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.infrastructure.persistence.learning.repository.HintQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UseHintEventListener {

    private final ProblemQueryService problemQueryService;
    private final HintHistoryCommandService hintHistoryCommandService;

    @TransactionalEventListener
    @Async("hintHistoryTaskExecutor")
    public void handleUseHintEvent(UseHintEvent event) {

        Long hintId = problemQueryService.findHintIdByProblemUuidAndLevel(
                event.problemUuid(),
                event.level()
        );

        HintHistory hintHistory = HintHistory.of(
                hintId,
                event.memberId()
        );

        hintHistoryCommandService.saveHintHistory(hintHistory);

    }

}
