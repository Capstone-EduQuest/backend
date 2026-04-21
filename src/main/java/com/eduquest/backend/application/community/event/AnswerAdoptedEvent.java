package com.eduquest.backend.application.community.event;

import java.util.UUID;

public record AnswerAdoptedEvent(
        UUID answerUuid,
        long rewardAmount
) {
    public static AnswerAdoptedEvent of(UUID answerUuid, long rewardAmount) {
        return new AnswerAdoptedEvent(answerUuid, rewardAmount);
    }
}

