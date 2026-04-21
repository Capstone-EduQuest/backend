package com.eduquest.backend.application.community.dto;

public record CreateQuestionCommand(
        String title,
        String content
) {
    public static CreateQuestionCommand of(String title, String content) {
        return new CreateQuestionCommand(title, content);
    }
}

