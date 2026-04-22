package com.eduquest.backend.presentation.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

public record AnswerResponse(
        UUID uuid,
        String content,
        UserInfo user,
        @JsonProperty("is_adopt")
        Boolean isAdopt,
        @JsonProperty("created_at")
        Instant createdAt
) {
    public static AnswerResponse of(UUID uuid, String content, UserInfo user, Boolean isAdopt, Instant createdAt) {
        return new AnswerResponse(uuid, content, user, isAdopt, createdAt);
    }
}

