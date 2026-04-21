package com.eduquest.backend.application.community.dto;

import java.time.Instant;
import java.util.UUID;

public record QuestionDetailResponse(
        UUID uuid,
        String title,
        UUID userUuid,
        String userNickname,
        Instant createdAt,
        String content,
        Boolean isAdopt,
        UUID adoptedAnswerUuid
) {
    public static QuestionDetailResponse of(UUID uuid, String title, UUID userUuid, String userNickname, Instant createdAt, String content, Boolean isAdopt, UUID adoptedAnswerUuid) {
        return new QuestionDetailResponse(uuid, title, userUuid, userNickname, createdAt, content, isAdopt, adoptedAnswerUuid);
    }
}

