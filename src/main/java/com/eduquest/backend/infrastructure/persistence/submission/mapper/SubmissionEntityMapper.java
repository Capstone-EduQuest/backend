package com.eduquest.backend.infrastructure.persistence.submission.mapper;

import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionEntity;
import org.springframework.stereotype.Component;

@Component
public class SubmissionEntityMapper {

    public SubmissionEntity toEntity(Long userId, Long problemId, String answer) {
        return SubmissionEntity.of(answer, userId, problemId);
    }

    public SubmissionEntity toEntity(com.eduquest.backend.domain.submission.model.Submission submission) {
        if (submission == null) {
            return null;
        }
        return SubmissionEntity.of(submission.getAnswer(), submission.getUserId(), submission.getProblemId());
    }

    public com.eduquest.backend.domain.submission.model.Submission toDomain(SubmissionEntity e) {
        if (e == null) {
            return null;
        }

        return com.eduquest.backend.domain.submission.model.Submission.of(
                e.getUuid(),
                e.getId(),
                e.getUserId(),
                e.getProblemId(),
                e.getAnswer(),
                e.getCreatedAt()
        );
    }

    public java.util.List<com.eduquest.backend.domain.submission.model.Submission> toDomainList(java.util.List<SubmissionEntity> entities) {
        if (entities == null) {
            return java.util.Collections.emptyList();
        }
        return entities.stream().map(this::toDomain).toList();
    }

}

