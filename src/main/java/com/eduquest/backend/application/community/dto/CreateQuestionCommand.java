package com.eduquest.backend.application.community.dto;

public record CreateQuestionCommand(
        String title,
        String content,
        String userId
) {
    public static CreateQuestionCommand of(String title, String content, String userId) {
        return new CreateQuestionCommand(title, content, userId);
    }
}

