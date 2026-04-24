package com.eduquest.backend.domain.bookmark.dto;

import java.util.List;
import java.util.Objects;

public record BookmarkList(
        int page,
        int size,
        String sort,
        Boolean isAsc,
        long total,
        List<BookmarkQuery.Summary> results
) {
    public BookmarkList {
        Objects.requireNonNull(results);
    }

    public static BookmarkList of(int page, int size, String sort, Boolean isAsc, long total, List<BookmarkQuery.Summary> results) {
        return new BookmarkList(page, size, sort, isAsc, total, results);
    }

}

