package com.eduquest.backend.domain.bookmark.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookmarkDto(
        Long id,
        UUID uuid,
        LocalDateTime createdAt,
        Long problemId,
        Long userId
) {
    public static BookmarkDto of(Long id, UUID uuid, LocalDateTime createdAt, Long problemId, Long userId) {
        return new BookmarkDto(id, uuid, createdAt, problemId, userId);
    }
}

