package com.eduquest.backend.presentation.learning.dto.response;

import java.util.List;

public record ProblemListResponse(int page, int size, String sort, Boolean isAsc, List<ProblemResponse> results) {

    public static ProblemListResponse of(int page, int size, String sort, Boolean isAsc, List<ProblemResponse> results) {
        return new ProblemListResponse(page, size, sort, isAsc, results);
    }

}

