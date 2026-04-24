package com.eduquest.backend.domain.bookmark.dto;

import java.util.List;
import java.util.Objects;

public record BookmarkListResult(
        int page,
        int size,
        String sort,
        Boolean isAsc,
        long total,
        List<BookmarkQuery.Summary> results
) {
    public BookmarkListResult {
        Objects.requireNonNull(results);
    }

    public static BookmarkListResult of(int page, int size, String sort, Boolean isAsc, long total, List<BookmarkQuery.Summary> results) {
        return new BookmarkListResult(page, size, sort, isAsc, total, results);
    }

}

