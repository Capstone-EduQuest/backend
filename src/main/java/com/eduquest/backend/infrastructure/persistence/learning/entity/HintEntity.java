package com.eduquest.backend.infrastructure.persistence.learning.entity;

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
@Table(name = "hint")
public class HintEntity extends BasicUpdateEntity {

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "point", nullable = false)
    private Long point;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder(access = AccessLevel.PROTECTED)
    public HintEntity(Long problemId, Integer level, Long point, String content) {
        this.problemId = problemId;
        this.level = level;
        this.point = point;
        this.content = content;
    }

    public static HintEntity of(Long problemId, Integer level, Long point, String content) {
        return HintEntity.builder().problemId(problemId).level(level).point(point).content(content).build();
    }

}


