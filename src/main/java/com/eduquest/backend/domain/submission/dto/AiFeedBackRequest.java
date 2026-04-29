package com.eduquest.backend.domain.submission.dto;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.Map;

@Builder(access = AccessLevel.PROTECTED)
public record AiFeedBackRequest(
        String problemText,
        String correctAnswer,
        String userAnswer,
        Map<String, String> metadata
) {

    public static AiFeedBackRequest of(String problemText, String correctAnswer, String userAnswer, Map<String, String> metadata) {
        return AiFeedBackRequest.builder()
                .problemText(problemText)
                .correctAnswer(correctAnswer)
                .userAnswer(userAnswer)
                .metadata(metadata)
                .build();
    }

}
