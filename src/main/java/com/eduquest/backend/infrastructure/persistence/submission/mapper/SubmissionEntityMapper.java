package com.eduquest.backend.infrastructure.persistence.submission.mapper;

import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SubmissionEntityMapper {

    public SubmissionEntity toEntity(Long userId, Long problemId, String answer) {
        return SubmissionEntity.of(answer, userId, problemId);
    }

    public SubmissionEntity toEntity(Submission submission) {
        if (submission == null) {
            return null;
        }
        return SubmissionEntity.of(submission.getAnswer(), submission.getUserId(), submission.getProblemId());
    }

    public Submission toDomain(SubmissionEntity e) {
        if (e == null) {
            return null;
        }

        return Submission.of(
                e.getUuid(),
                e.getId(),
                e.getUserId(),
                e.getProblemId(),
                e.getAnswer(),
                e.getCreatedAt()
        );
    }

    public List<Submission> toDomainList(List<SubmissionEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDomain).toList();
    }

}

