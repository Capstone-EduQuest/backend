package com.eduquest.backend.application.community.dto;

public record AnswerListQuery(
        int page,
        int size,
        Boolean isAsc
) {
    public static AnswerListQuery of(int page, int size, Boolean isAsc) {
        return new AnswerListQuery(page, size, isAsc);
    }
}

