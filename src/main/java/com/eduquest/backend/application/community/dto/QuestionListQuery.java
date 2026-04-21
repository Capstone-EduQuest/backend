package com.eduquest.backend.application.community.dto;

public record QuestionListQuery(
        int page,
        int size,
        String sort,
        Boolean isAsc
) {
    public static QuestionListQuery of(int page, int size, String sort, Boolean isAsc) {
        return new QuestionListQuery(page, size, sort, isAsc);
    }
}

