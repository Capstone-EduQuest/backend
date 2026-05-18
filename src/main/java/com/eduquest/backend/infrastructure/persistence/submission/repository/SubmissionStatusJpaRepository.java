package com.eduquest.backend.infrastructure.persistence.submission.repository;

import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmissionStatusJpaRepository extends JpaRepository<SubmissionStatusEntity, Long> {

    Optional<SubmissionStatusEntity> findBySubmissionId(Long submissionId);

}
