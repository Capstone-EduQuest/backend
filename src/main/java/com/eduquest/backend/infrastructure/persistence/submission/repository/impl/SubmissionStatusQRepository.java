package com.eduquest.backend.infrastructure.persistence.submission.repository.impl;

import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionStatusEntity;

import java.util.Optional;
import java.util.UUID;

public interface SubmissionStatusQRepository {

    Optional<SubmissionStatusEntity> findBySubmissionUuid(UUID submissionUuid);

}
