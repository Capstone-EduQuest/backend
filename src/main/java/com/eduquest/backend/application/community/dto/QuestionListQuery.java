package com.eduquest.backend.application.community.dto;

public record QuestionListQuery(
        int page,
        int size,
        String sort,
        Boolean isAsc,
        String searchBy,
        String keyword
) {
    public static QuestionListQuery of(int page, int size, String sort, Boolean isAsc, String searchBy, String keyword) {
        return new QuestionListQuery(page, size, sort, isAsc, searchBy, keyword);
    }
}

