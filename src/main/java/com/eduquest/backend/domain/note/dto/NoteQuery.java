package com.eduquest.backend.domain.note.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class NoteQuery {

    public record Detail(
            UUID uuid,
            Long id,
            Long userId,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static Detail of(UUID uuid, Long id, Long userId, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
            return new Detail(uuid, id, userId, title, content, createdAt, updatedAt);
        }
    }

}

