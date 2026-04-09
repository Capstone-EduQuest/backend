package com.eduquest.backend.domain.learning.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Hint {

    private Long id;
    private UUID uuid;
    private Integer level;
    private Long point;
    private String content;
    private Long problemId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(access = AccessLevel.PROTECTED)
    public Hint(Integer level, Long point, String content, Long problemId) {
        this.level = level;
        this.point = point;
        this.content = content;
        this.problemId = problemId;
    }

    public static Hint of(Integer level, Long point, String content, Long problemId) {
        return Hint.builder().level(level).point(point).content(content).problemId(problemId).build();
    }

    public static Hint of(Integer level, Long point, String content) {
        return Hint.builder().level(level).point(point).content(content).build();
    }

}

