package com.eduquest.backend.application.submission.dto;

import java.time.LocalDateTime;

public record EvaluationInfo(Boolean isCorrect, LocalDateTime createdAt) {

    public static EvaluationInfo of(Boolean isCorrect, LocalDateTime createdAt) {
        return new EvaluationInfo(isCorrect, createdAt);
    }

}

