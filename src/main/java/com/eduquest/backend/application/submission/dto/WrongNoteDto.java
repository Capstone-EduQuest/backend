package com.eduquest.backend.application.submission.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = lombok.AccessLevel.PROTECTED)
public record WrongNoteDto(
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

    public static WrongNoteDto of(
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
        return WrongNoteDto.builder()
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
