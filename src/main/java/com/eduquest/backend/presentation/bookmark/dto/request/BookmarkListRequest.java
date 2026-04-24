package com.eduquest.backend.presentation.bookmark.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

public record BookmarkListRequest(
        @Min(0) Integer page,
        @Min(1) Integer size,
        String sort,
        @JsonProperty("is_asc")
        Boolean isAsc
) {
    public int pageValue() {
        if (page == null) {
            return 0;
        }
        return page;
    }

    public int sizeValue() {
        if (size == null) {
            return 20;
        }
        return size;
    }

    public String sortValue() {
        if (sort == null) {
            return "created_at";
        }
        return sort;
    }

    public boolean isAscValue() {
        if (isAsc == null) {
            return false;
        }
        return isAsc;
    }
}

