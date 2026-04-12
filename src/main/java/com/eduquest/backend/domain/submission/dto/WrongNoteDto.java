package com.eduquest.backend.domain.submission.dto;

import java.time.LocalDateTime;

public class WrongNoteDto {

    public record Detail(
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
        public static Detail of(Long id, Long userId, Long problemId, String wrongAnswer, String aiExplanation, Boolean isReviewed, LocalDateTime nextReviewAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
            return new Detail(id, userId, problemId, wrongAnswer, aiExplanation, isReviewed, nextReviewAt, createdAt, updatedAt);
        }
    }

}

