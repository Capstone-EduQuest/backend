package com.eduquest.backend.presentation.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AnswerListResponse(
        int page,
        int size,
        String sort,
        @JsonProperty("is_asc")
        Boolean isAsc,
        @JsonProperty("results")
        List<AnswerSummary> resultsList
) {
    public static AnswerListResponse of(int page, int size, String sort, Boolean isAsc, List<AnswerSummary> resultsList) {
        return new AnswerListResponse(page, size, sort, isAsc, resultsList);
    }
}

