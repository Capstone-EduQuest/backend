package com.eduquest.backend.application.community.dto;

import java.util.UUID;

public record CreateAnswerCommand(
        UUID questionUuid,
        String content,
        String userId
) {
    public static CreateAnswerCommand of(UUID questionUuid, String content, String userId) {
        return new CreateAnswerCommand(questionUuid, content, userId);
    }
}

