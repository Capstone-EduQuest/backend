package com.eduquest.backend.application.community.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record QuestionDetailResponse(
        UUID uuid,
        String title,
        UUID userUuid,
        String userNickname,
        LocalDateTime createdAt,
        String content,
        Boolean isAdopt,
        UUID adoptedAnswerUuid
) {
    public static QuestionDetailResponse of(UUID uuid, String title, UUID userUuid, String userNickname, LocalDateTime createdAt, String content, Boolean isAdopt, UUID adoptedAnswerUuid) {
        return new QuestionDetailResponse(uuid, title, userUuid, userNickname, createdAt, content, isAdopt, adoptedAnswerUuid);
    }
}

