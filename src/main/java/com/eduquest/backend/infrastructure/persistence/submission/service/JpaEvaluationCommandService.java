package com.eduquest.backend.infrastructure.persistence.submission.service;

import com.eduquest.backend.domain.submission.service.EvaluationCommandService;
import com.eduquest.backend.infrastructure.persistence.submission.entity.EvaluationEntity;
import com.eduquest.backend.infrastructure.persistence.submission.repository.EvaluationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JpaEvaluationCommandService implements EvaluationCommandService {

    private final EvaluationJpaRepository evaluationJpaRepository;

    @Override
    @Transactional
    public Long saveEvaluation(Boolean isCorrect, Long submissionId) {
        EvaluationEntity evaluation = EvaluationEntity.of(isCorrect, submissionId);
        return evaluationJpaRepository.save(evaluation).getId();
    }
}

