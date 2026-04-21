package com.eduquest.backend.domain.community.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    private Long id;
    private UUID uuid;
    private String content;
    private Boolean isAdopted;
    private LocalDateTime createdAt;
    private Long userId;
    private Long communityPostId;

    @Builder(access = AccessLevel.PROTECTED)
    public Answer(Long id, UUID uuid, String content, Boolean isAdopted, LocalDateTime createdAt, Long userId, Long communityPostId) {
        this.id = id;
        this.uuid = uuid;
        this.content = content;
        this.isAdopted = isAdopted;
        this.createdAt = createdAt;
        this.userId = userId;
        this.communityPostId = communityPostId;
    }

    public static Answer of(String content, Long userId, Long communityPostId) {
        return Answer.builder()
                .content(content)
                .isAdopted(false)
                .userId(userId)
                .communityPostId(communityPostId)
                .build();
    }

    public static Answer of(Long id, UUID uuid, String content, Boolean isAdopted, LocalDateTime createdAt, Long userId, Long communityPostId) {
        return Answer.builder()
                .id(id)
                .uuid(uuid)
                .content(content)
                .isAdopted(isAdopted)
                .createdAt(createdAt)
                .userId(userId)
                .communityPostId(communityPostId)
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void adopt() {
        this.isAdopted = true;
    }

    public void revokeAdopt() {
        this.isAdopted = false;
    }

}

