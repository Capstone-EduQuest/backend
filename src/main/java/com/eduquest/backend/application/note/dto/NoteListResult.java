package com.eduquest.backend.application.note.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record NoteListResult(
        int page,
        int size,
        String sort,
        Boolean isAsc,
        long total,
        List<Item> results
) {
    public static NoteListResult of(int page, int size, String sort, Boolean isAsc, long total, List<Item> results) {
        return new NoteListResult(page, size, sort, isAsc, total, results);
    }

    public record Item(
            UUID uuid,
            Long id,
            UUID authorUuid,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static Item of(UUID uuid, Long id, UUID authorUuid, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
            return new Item(uuid, id, authorUuid, title, content, createdAt, updatedAt);
        }
    }

}

