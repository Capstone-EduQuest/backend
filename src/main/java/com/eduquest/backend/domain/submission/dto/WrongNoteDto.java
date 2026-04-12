package com.eduquest.backend.domain.submission.dto;

import java.time.LocalDateTime;

public class WrongNoteDto {

    public record Detail(
            java.util.UUID uuid,
            Long id,
            Long userId,
            Long problemId,
            String wrongAnswer,
            String aiExplanation,
            Boolean isReviewed,
            LocalDateTime nextReviewAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static Detail of(java.util.UUID uuid, Long id, Long userId, Long problemId, String wrongAnswer, String aiExplanation, Boolean isReviewed, LocalDateTime nextReviewAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
            return new Detail(uuid, id, userId, problemId, wrongAnswer, aiExplanation, isReviewed, nextReviewAt, createdAt, updatedAt);
        }
    }

}

