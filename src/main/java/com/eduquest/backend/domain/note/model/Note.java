package com.eduquest.backend.domain.note.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Note {

    private Long id;
    private UUID uuid;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(access = AccessLevel.PROTECTED)
    public Note(Long userId, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Note of(Long userId, String title, String content) {
        return Note.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Note of(UUID uuid, Long id, Long userId, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        Note note = Note.of(userId, title, content);
        note.uuid = uuid;
        note.id = id;
        note.createdAt = createdAt;
        note.updatedAt = updatedAt;
        return note;
    }

    public void update(String title, String content, LocalDateTime updatedAt) {
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
    }

}

