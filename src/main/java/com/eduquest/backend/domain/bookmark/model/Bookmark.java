package com.eduquest.backend.domain.bookmark.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Bookmark {

    private final Long id;
    private final UUID uuid;
    private final LocalDateTime createdAt;
    private final Long problemId;
    private final Long userId;

    private Bookmark(Long id, UUID uuid, LocalDateTime createdAt, Long problemId, Long userId) {
        this.id = id;
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.problemId = problemId;
        this.userId = userId;
    }

    public static Bookmark createNew(Long problemId, Long userId) {
        Objects.requireNonNull(problemId, "problemId must not be null");
        Objects.requireNonNull(userId, "userId must not be null");
        return new Bookmark(null, UUID.randomUUID(), LocalDateTime.now(), problemId, userId);
    }

    public static Bookmark of(Long id, UUID uuid, LocalDateTime createdAt, Long problemId, Long userId) {
        return new Bookmark(id, uuid, createdAt, problemId, userId);
    }

    public Long id() {
        return id;
    }

    public UUID uuid() {
        return uuid;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public Long problemId() {
        return problemId;
    }

    public Long userId() {
        return userId;
    }

}

