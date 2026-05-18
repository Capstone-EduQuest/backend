package com.eduquest.backend.infrastructure.persistence.submission.repository;

import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionStatusEntity;
import com.eduquest.backend.infrastructure.persistence.submission.repository.impl.SubmissionStatusQRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionStatusQueryRepository extends JpaRepository<SubmissionStatusEntity, Long>, SubmissionStatusQRepository {

    Optional<SubmissionStatusEntity> findBySubmissionId(Long submissionId);

}
