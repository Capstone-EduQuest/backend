package com.eduquest.backend.presentation.submission.dto.response;

import lombok.Builder;

import java.util.List;

@Builder(access = lombok.AccessLevel.PROTECTED)
public record WrongNoteListResponse(
        int page,
        int size,
        String sort,
        boolean isAsc,
        long total,
        List<WrongNoteResponse> results
) {

    public static WrongNoteListResponse of(
            int page,
            int size,
            String sort,
            boolean isAsc,
            long total,
            List<WrongNoteResponse> results
    ) {
        return WrongNoteListResponse.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .isAsc(isAsc)
                .total(total)
                .results(results)
                .build();
    }

    public static record WrongNoteList(int page, int size, String sort, boolean isAsc, long total, List<WrongNoteResponse> results) {
        public static WrongNoteList of(int page, int size, String sort, boolean isAsc, long total, List<WrongNoteResponse> results) {
            return new WrongNoteList(page, size, sort, isAsc, total, results);
        }
    }

}

