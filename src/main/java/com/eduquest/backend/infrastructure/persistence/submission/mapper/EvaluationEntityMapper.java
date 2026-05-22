package com.eduquest.backend.infrastructure.persistence.submission.mapper;

import com.eduquest.backend.domain.submission.model.Evaluation;
import com.eduquest.backend.infrastructure.persistence.submission.entity.EvaluationEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class EvaluationEntityMapper {

    public EvaluationEntity toEntity(Boolean isCorrect, Long submissionId) {
        return EvaluationEntity.of(isCorrect, submissionId);
    }

    public EvaluationEntity toEntity(Evaluation evaluation) {
        if (evaluation == null) {
            return null;
        }
        return EvaluationEntity.of(evaluation.getIsCorrect(), evaluation.getSubmissionId());
    }

    public Evaluation toDomain(EvaluationEntity e) {
        if (e == null) {
            return null;
        }

        return Evaluation.of(
                e.getUuid(),
                e.getId(),
                e.getSubmissionId(),
                e.getIsCorrect(),
                e.getCreatedAt()
        );
    }

    public List<Evaluation> toDomainList(List<EvaluationEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDomain).toList();
    }

}

