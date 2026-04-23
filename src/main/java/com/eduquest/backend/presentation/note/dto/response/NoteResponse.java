package com.eduquest.backend.presentation.note.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = lombok.AccessLevel.PROTECTED)
public record NoteResponse(
        UUID uuid,
        String title,
        String content,
        UUID authorUuid,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static NoteResponse of(
            UUID uuid,
            String title,
            String content,
            UUID authorUuid,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return NoteResponse.builder()
                .uuid(uuid)
                .title(title)
                .content(content)
                .authorUuid(authorUuid)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}

