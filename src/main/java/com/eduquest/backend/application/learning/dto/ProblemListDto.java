package com.eduquest.backend.application.learning.dto;

import java.util.List;

public record ProblemListDto(
        int page,
        int size,
        String sort,
        Boolean isAsc,
        List<ProblemDto> results
) {

    public static ProblemListDto of(int page, int size, String sort, Boolean isAsc, List<ProblemDto> results) {
        return new ProblemListDto(page, size, sort, isAsc, results);
    }

}

