
package com.eduquest.backend.presentation.note.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record NoteListRequest(
        @Min(value = 0, message = "page는 0 이상이어야 합니다.") int page,
        @Min(value = 1, message = "size는 1 이상이어야 합니다.") int size,
        String sort,
        @JsonProperty("is_asc") @NotNull(message = "isAsc는 필수입니다.") Boolean isAsc,
        String searchBy,
        @JsonProperty("keyword") String keyword
) {

    public static NoteListRequest of(int page, int size, String sort, Boolean isAsc, String searchBy, String keyword) {
        return new NoteListRequest(page, size, sort, isAsc, searchBy, keyword);
    }

}

