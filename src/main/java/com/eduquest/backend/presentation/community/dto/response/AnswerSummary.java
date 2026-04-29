package com.eduquest.backend.presentation.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record AnswerSummary(
        UUID uuid,
        String content,
        UserInfo user,
        @JsonProperty("is_adopt")
        Boolean isAdopt,
        @JsonProperty("created_at")
        LocalDateTime createdAt
) {
    public static AnswerSummary of(UUID uuid, String content, UserInfo user, Boolean isAdopt, LocalDateTime createdAt) {
        return new AnswerSummary(uuid, content, user, isAdopt, createdAt);
    }
}

