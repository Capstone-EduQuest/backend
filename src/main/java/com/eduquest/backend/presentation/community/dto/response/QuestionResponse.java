package com.eduquest.backend.presentation.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record QuestionResponse(
        UUID uuid,
        String title,
        UserInfo user,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        String content,
        @JsonProperty("is_adopt")
        Boolean isAdopt,
        @JsonProperty("adopted_answer")
        UUID adoptedAnswer
) {
    public static QuestionResponse of(UUID uuid, String title, UserInfo user, LocalDateTime createdAt, String content, Boolean isAdopt, UUID adoptedAnswer) {
        return new QuestionResponse(uuid, title, user, createdAt, content, isAdopt, adoptedAnswer);
    }
}

