package com.eduquest.backend.presentation.learning.dto.response;

import java.util.List;

public record StageListResponse(int page, int size, String sort, Boolean isAsc, List<StageResponse> results) {

    public static StageListResponse of(int page, int size, String sort, Boolean isAsc, List<StageResponse> results) {
        return new StageListResponse(page, size, sort, isAsc, results);
    }

}

