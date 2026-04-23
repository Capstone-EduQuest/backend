package com.eduquest.backend.infrastructure.persistence.note.entity;

import com.eduquest.backend.infrastructure.persistence.common.entity.BasicUpdateEntity;
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
@Table(name = "note")
public class NoteEntity extends BasicUpdateEntity {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder(access = AccessLevel.PROTECTED)
    public NoteEntity(String title, String content, Long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public static NoteEntity of(String title, String content, Long userId) {
        return NoteEntity.builder()
                .title(title)
                .content(content)
                .userId(userId)
                .build();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}

