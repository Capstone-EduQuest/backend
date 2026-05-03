package com.eduquest.backend.infrastructure.persistence.submission.repository;

import com.eduquest.backend.infrastructure.persistence.submission.entity.EvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationJpaRepository extends JpaRepository<EvaluationEntity, Long> {

	List<EvaluationEntity> findBySubmissionIdIn(List<Long> submissionIds);

	Optional<EvaluationEntity> findBySubmissionId(Long submissionId);

}

