package com.eduquest.backend.application.community.service;

import com.eduquest.backend.application.community.dto.AnswerListQuery;
import com.eduquest.backend.application.community.dto.AnswerListResult;
import com.eduquest.backend.application.community.dto.CreateAnswerCommand;
import com.eduquest.backend.application.community.event.AdoptConfig;
import com.eduquest.backend.application.community.event.AnswerAdoptedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final ApplicationEventPublisher eventPublisher;

    public UUID createAnswer(CreateAnswerCommand command) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public AnswerListResult findAnswersByQuestionUuid(UUID questionUuid, AnswerListQuery query) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void deleteAnswerByUuid(UUID answerUuid) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void adoptAnswer(UUID answerUuid) {
        AnswerAdoptedEvent event = AnswerAdoptedEvent.of(answerUuid, AdoptConfig.ADOPT_REWARD);
        eventPublisher.publishEvent(event);
    }

}

