package com.eduquest.backend.infrastructure.persistence.submission.service;

import com.eduquest.backend.domain.submission.model.Evaluation;
import com.eduquest.backend.domain.submission.service.EvaluationQueryService;
import com.eduquest.backend.infrastructure.persistence.submission.entity.EvaluationEntity;
import com.eduquest.backend.infrastructure.persistence.submission.repository.EvaluationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaEvaluationQueryService implements EvaluationQueryService {

    private final EvaluationJpaRepository evaluationJpaRepository;

    @Override
    public List<Evaluation> findBySubmissionIds(List<Long> submissionIds) {
        if (submissionIds == null || submissionIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<EvaluationEntity> entities = evaluationJpaRepository.findBySubmissionIdIn(submissionIds);
        return entities.stream()
                .map(e -> Evaluation.of(e.getUuid(), e.getId(), e.getSubmissionId(), e.getIsCorrect(), e.getCreatedAt()))
                .collect(Collectors.toList());
    }

}

