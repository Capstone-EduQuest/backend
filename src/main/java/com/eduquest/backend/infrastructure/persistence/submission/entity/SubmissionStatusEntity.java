package com.eduquest.backend.infrastructure.persistence.submission.entity;

import com.eduquest.backend.domain.submission.model.enums.SubmissionStatus;
import com.eduquest.backend.infrastructure.persistence.common.entity.BasicUpdateEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "submission_status")
public class SubmissionStatusEntity extends BasicUpdateEntity {

    @Column(name = "submission_id", nullable = false)
    private Long submissionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubmissionStatus status;

    @Column(name = "try_count", nullable = false)
    private Integer tryCount = 0;

    @Builder(access = AccessLevel.PROTECTED)
    public SubmissionStatusEntity(Long submissionId, SubmissionStatus status, Integer tryCount) {
        this.submissionId = submissionId;
        this.status = status;
        this.tryCount = tryCount;
    }

    public static SubmissionStatusEntity of(Long submissionId, SubmissionStatus status, Integer tryCount) {
        return SubmissionStatusEntity.builder()
                .submissionId(submissionId)
                .status(status)
                .tryCount(tryCount)
                .build();
    }

    public void increaseTryCount() {
        this.tryCount++;
    }

    public void changeStatus(SubmissionStatus status) {
        this.status = status;
    }

}
