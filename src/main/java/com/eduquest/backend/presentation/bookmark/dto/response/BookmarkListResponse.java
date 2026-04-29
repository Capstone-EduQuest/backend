package com.eduquest.backend.presentation.bookmark.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record BookmarkListResponse(
        int page,
        int size,
        String sort,
        @JsonProperty("is_asc")
        boolean isAsc,
        long total,
        List<Result> results
) {
    public static BookmarkListResponse of(int page, int size, String sort, boolean isAsc, long total, List<Result> results) {
        return new BookmarkListResponse(page, size, sort, isAsc, total, results);
    }

    public record Result(
            String stage,
            String type,
            Integer number,
            @JsonProperty("problem_uuid") UUID problemUuid
    ) {
        public static Result of(String stage, String type, Integer number, UUID problemUuid) {
            return new Result(stage, type, number, problemUuid);
        }
    }
}

