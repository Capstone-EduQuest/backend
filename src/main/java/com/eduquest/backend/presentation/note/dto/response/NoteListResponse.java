package com.eduquest.backend.presentation.note.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder(access = lombok.AccessLevel.PROTECTED)
public record NoteListResponse(
        int page,
        int size,
        String sort,
        @JsonProperty("is_asc") boolean isAsc,
        long total,
        List<NoteResponse> results
) {

    public static NoteListResponse of(
            int page,
            int size,
            String sort,
            boolean isAsc,
            long total,
            List<NoteResponse> results
    ) {
        return NoteListResponse.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .isAsc(isAsc)
                .total(total)
                .results(results)
                .build();
    }

    public record NoteList(
            int page,
            int size,
            String sort,
            @JsonProperty("is_asc") boolean isAsc,
            long total,
            List<NoteResponse> results
    ) {
        public static NoteList of(int page, int size, String sort, boolean isAsc, long total, List<NoteResponse> results) {
            return new NoteList(page, size, sort, isAsc, total, results);
        }
    }
}

