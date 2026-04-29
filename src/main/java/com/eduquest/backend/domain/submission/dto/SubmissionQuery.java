package com.eduquest.backend.domain.submission.dto;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class SubmissionQuery {

    public record Summary(UUID uuid, Long problemId, Boolean isCorrect, LocalDateTime createdAt) {
        public static Summary of(UUID uuid, Long problemId, Boolean isCorrect, LocalDateTime createdAt) {
            return new Summary(uuid, problemId, isCorrect, createdAt);
        }
    }

    public record Detail(Long id, UUID uuid, Long userId, Long problemId, String answer, Boolean isCorrect, LocalDateTime createdAt) {
        public static Detail of(Long id, UUID uuid, Long userId, Long problemId, String answer, Boolean isCorrect, LocalDateTime createdAt) {
            return new Detail(id, uuid, userId, problemId, answer, isCorrect, createdAt);
        }
    }

}

