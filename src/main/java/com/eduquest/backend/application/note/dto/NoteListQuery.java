package com.eduquest.backend.application.note.dto;

public record NoteListQuery(
        int page,
        int size,
        String sort,
        Boolean isAsc,
        String searchBy,
        String keyword
) {
    public static NoteListQuery of(int page, int size, String sort, Boolean isAsc, String searchBy, String keyword) {
        return new NoteListQuery(page, size, sort, isAsc, searchBy, keyword);
    }
}

