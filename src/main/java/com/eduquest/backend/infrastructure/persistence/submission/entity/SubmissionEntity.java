package com.eduquest.backend.infrastructure.persistence.submission.entity;

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
@Table(name = "submission")
public class SubmissionEntity extends BasicEntity {

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Builder(access = AccessLevel.PROTECTED)
    public SubmissionEntity(String answer, Long userId, Long problemId) {
        this.answer = answer;
        this.userId = userId;
        this.problemId = problemId;
    }

    public static SubmissionEntity of(String answer, Long userId, Long problemId) {
        return SubmissionEntity.builder()
                .answer(answer)
                .userId(userId)
                .problemId(problemId)
                .build();
    }

}

