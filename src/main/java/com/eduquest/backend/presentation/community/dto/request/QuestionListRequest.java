package com.eduquest.backend.presentation.community.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record QuestionListRequest(
        @NotNull(message = "페이지 번호는 필수입니다.")
        Integer page,
        @NotNull(message = "페이지 크기는 필수입니다.")
        Integer size,
        String sort,
        @JsonProperty("is_asc")
        Boolean isAsc
) {

    public static QuestionListRequest of(int page, int size, String sort, Boolean isAsc) {
        return new QuestionListRequest(page, size, sort, isAsc);
    }

}

