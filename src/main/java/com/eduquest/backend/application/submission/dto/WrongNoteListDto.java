package com.eduquest.backend.application.submission.dto;

import lombok.Builder;

import java.util.List;

@Builder(access = lombok.AccessLevel.PROTECTED)
public record WrongNoteListDto(
        int page,
        int size,
        String sort,
        boolean isAsc,
        long total,
        List<WrongNoteDto> results
) {

    public static WrongNoteListDto of(
            int page,
            int size,
            String sort,
            boolean isAsc,
            long total,
            List<WrongNoteDto> results
    ) {
        return WrongNoteListDto.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .isAsc(isAsc)
                .total(total)
                .results(results)
                .build();
    }

}
