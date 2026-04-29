package com.eduquest.backend.domain.submission.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class WrongNoteQuery {

    public record Detail(
            UUID uuid,
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
        public static Detail of(UUID uuid, Long id, Long userId, Long problemId, String wrongAnswer, String aiExplanation, Boolean isReviewed, LocalDateTime nextReviewAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
            return new Detail(uuid, id, userId, problemId, wrongAnswer, aiExplanation, isReviewed, nextReviewAt, createdAt, updatedAt);
        }
    }

}

