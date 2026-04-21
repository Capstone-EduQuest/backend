package com.eduquest.backend.presentation.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

public record QuestionSummary(
        UUID uuid,
        String title,
        UserInfo user,
        @JsonProperty("created_at")
        Instant createdAt
) {
    public static QuestionSummary of(UUID uuid, String title, UserInfo user, Instant createdAt) {
        return new QuestionSummary(uuid, title, user, createdAt);
    }
}

