package com.eduquest.backend.application.reward.listener;

import com.eduquest.backend.domain.community.event.AnswerAdoptedEvent;
import com.eduquest.backend.domain.community.model.Answer;
import com.eduquest.backend.domain.community.service.AnswerQueryService;
import com.eduquest.backend.domain.reward.event.GrantPointEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnswerAdoptedEventListener {

    private final AnswerQueryService answerQueryService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @EventListener
    public void handleAnswerAdopted(AnswerAdoptedEvent event) {
        UUID answerUuid = event.answerUuid();

        Answer answer = answerQueryService.findAnswerByUuid(answerUuid);
        if (answer == null) {
            log.warn("AnswerAdoptedEvent received but answer not found: {}", answerUuid);
            return;
        }

        Long memberId = answer.getUserId();
        if (memberId == null) {
            log.warn("AnswerAdoptedEvent: answer has no userId: {}", answerUuid);
            return;
        }

        // publish GrantPointEvent for reward handling
        GrantPointEvent grantEvent = GrantPointEvent.of(memberId, event.rewardAmount(), "ADOPT_REWARD:" + answerUuid);
        eventPublisher.publishEvent(grantEvent);
        log.info("Published GrantPointEvent for adopted answer: answerUuid={}, memberId={}, amount={}", answerUuid, memberId, event.rewardAmount());
    }
}

