package com.eduquest.backend.presentation.identity.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserListRequest(
        Integer page,
        Integer size,
        String sort,
        @JsonProperty("is_asc")
        Boolean isAsc
) {

    public static UserListRequest of(int page, int size, String sort, Boolean isAsc) {
        return new UserListRequest(page, size, sort, isAsc);
    }

}
