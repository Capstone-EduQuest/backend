package com.eduquest.backend.application.bookmark.dto;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record BookmarkListResult(
        int page,
        int size,
        String sort,
        Boolean isAsc,
        long total,
        List<Item> results
) {
    public BookmarkListResult {
        Objects.requireNonNull(results);
    }

    public static BookmarkListResult of(int page, int size, String sort, Boolean isAsc, long total, List<Item> results) {
        return new BookmarkListResult(page, size, sort, isAsc, total, results);
    }

    public record Item(
            String stage,
            String type,
            Integer number,
            UUID problemUuid
    ) {
        public static Item of(String stage, String type, Integer number, UUID problemUuid) {
            return new Item(stage, type, number, problemUuid);
        }
    }

}

