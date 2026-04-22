package com.eduquest.backend.infrastructure.persistence.community.entity;

import com.eduquest.backend.infrastructure.persistence.common.entity.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "community_post")
public class CommunityPostEntity extends BasicEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_adopted", nullable = false)
    private Boolean isAdopted;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder(access = AccessLevel.PROTECTED)
    public CommunityPostEntity(String title, String content, Boolean isAdopted, Long userId) {
        this.title = title;
        this.content = content;
        this.isAdopted = isAdopted;
        this.userId = userId;
    }

    public static CommunityPostEntity of(String title, String content, Long userId) {
        return CommunityPostEntity.builder()
                .title(title)
                .content(content)
                .isAdopted(false)
                .userId(userId)
                .build();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void adopt() {
        this.isAdopted = true;
    }

    public void revokeAdopt() {
        this.isAdopted = false;
    }
}

