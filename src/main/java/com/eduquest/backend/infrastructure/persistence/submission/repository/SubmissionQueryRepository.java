package com.eduquest.backend.infrastructure.persistence.submission.repository;

import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionQueryRepository extends JpaRepository<SubmissionEntity, Long> {

    List<SubmissionEntity> findByProblemId(Long problemId);

    List<SubmissionEntity> findByUserId(Long userId);

}
