package com.eduquest.backend.infrastructure.persistence.bookmark.entity;

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
@Table(name = "bookmark")
public class BookmarkEntity extends BasicEntity {

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder(access = AccessLevel.PROTECTED)
    public BookmarkEntity(Long problemId, Long userId) {
        this.problemId = problemId;
        this.userId = userId;
    }

    public static BookmarkEntity of(Long problemId, Long userId) {
        return BookmarkEntity.builder()
                .problemId(problemId)
                .userId(userId)
                .build();
    }

}

