package com.eduquest.backend.application.note.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NoteDto(
        UUID uuid,
        Long id,
        UUID authorUuid,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static NoteDto of(UUID uuid, Long id, UUID authorUuid, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new NoteDto(uuid, id, authorUuid, title, content, createdAt, updatedAt);
    }
}

