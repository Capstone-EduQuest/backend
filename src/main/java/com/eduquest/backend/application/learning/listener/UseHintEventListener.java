package com.eduquest.backend.application.learning.listener;

import com.eduquest.backend.domain.learning.event.UseHintEvent;
import com.eduquest.backend.domain.learning.model.HintHistory;
import com.eduquest.backend.domain.learning.service.HintHistoryCommandService;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UseHintEventListener {

    private final ProblemQueryService problemQueryService;
    private final HintHistoryCommandService hintHistoryCommandService;

    @EventListener
    public void handleUseHintEvent(UseHintEvent event) {

        HintHistory hintHistory = HintHistory.of(
                event.hintId(),
                event.memberId()
        );

        hintHistoryCommandService.saveHintHistory(hintHistory);

    }

}
