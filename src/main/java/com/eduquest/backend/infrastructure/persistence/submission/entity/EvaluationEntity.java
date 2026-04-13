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
@Table(name = "evaluation")
public class EvaluationEntity extends BasicEntity {

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @Column(name = "submission_id", nullable = false)
    private Long submissionId;

    @Builder(access = AccessLevel.PROTECTED)
    public EvaluationEntity(Boolean isCorrect, Long submissionId) {
        this.isCorrect = isCorrect;
        this.submissionId = submissionId;
    }

    public static EvaluationEntity of(Boolean isCorrect, Long submissionId) {
        return EvaluationEntity.builder()
                .isCorrect(isCorrect)
                .submissionId(submissionId)
                .build();
    }

}

