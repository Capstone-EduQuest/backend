package com.eduquest.backend.presentation.submission.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = lombok.AccessLevel.PROTECTED)
public record WrongNoteResponse(
        Long id,
        Long problemId,
        UUID userUuid,
        String wrongAnswer,
        String feedback,
        Boolean isReviewed,
        LocalDateTime lastSubmittedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static WrongNoteResponse of(
            Long id,
            Long problemId,
            UUID userUuid,
            String wrongAnswer,
            String feedback,
            Boolean isReviewed,
            LocalDateTime lastSubmittedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return WrongNoteResponse.builder()
                .id(id)
                .problemId(problemId)
                .userUuid(userUuid)
                .wrongAnswer(wrongAnswer)
                .feedback(feedback)
                .isReviewed(isReviewed)
                .lastSubmittedAt(lastSubmittedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

}

