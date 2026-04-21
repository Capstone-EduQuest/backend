package com.eduquest.backend.presentation.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record QuestionListResponse(
        int page,
        int size,
        String sort,
        @JsonProperty("is_asc")
        Boolean isAsc,
        @JsonProperty("results")
        List<QuestionSummary> resultsList
) {
    public static QuestionListResponse of(int page, int size, String sort, Boolean isAsc, List<QuestionSummary> resultsList) {
        return new QuestionListResponse(page, size, sort, isAsc, resultsList);
    }
}

