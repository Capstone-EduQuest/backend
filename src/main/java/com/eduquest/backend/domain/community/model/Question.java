package com.eduquest.backend.domain.community.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    private Long id;
    private UUID uuid;
    private String title;
    private String content;
    private Boolean isAdopted;
    private LocalDateTime createdAt;
    private Long userId;

    @Builder(access = AccessLevel.PROTECTED)
    public Question(Long id, UUID uuid, String title, String content, Boolean isAdopted, LocalDateTime createdAt, Long userId) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.content = content;
        this.isAdopted = isAdopted;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public static Question of(String title, String content, Long userId) {
        return Question.builder()
                .title(title)
                .content(content)
                .isAdopted(false)
                .userId(userId)
                .build();
    }

    public static Question of(Long id, UUID uuid, String title, String content, Boolean isAdopted, LocalDateTime createdAt, Long userId
    ) {
        return Question.builder()
                .id(id)
                .uuid(uuid)
                .title(title)
                .content(content)
                .isAdopted(isAdopted)
                .createdAt(createdAt)
                .userId(userId)
                .build();
    }

    public void updateContent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void markAdopted() {
        this.isAdopted = true;
    }

    public void markUnadopted() {
        this.isAdopted = false;
    }

}

