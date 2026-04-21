package com.eduquest.backend.application.community.dto;

import java.util.UUID;

public record CreateAnswerCommand(
        UUID questionUuid,
        String content
) {
    public static CreateAnswerCommand of(UUID questionUuid, String content) {
        return new CreateAnswerCommand(questionUuid, content);
    }
}

