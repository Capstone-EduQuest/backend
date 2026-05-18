package com.eduquest.backend.infrastructure.persistence.submission.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.model.enums.SubmissionStatus;
import com.eduquest.backend.domain.submission.service.SubmissionCommandService;
import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionEntity;
import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionStatusEntity;
import com.eduquest.backend.infrastructure.persistence.submission.exception.SubmissionDatabaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.submission.mapper.SubmissionEntityMapper;
import com.eduquest.backend.infrastructure.persistence.submission.repository.SubmissionJpaRepository;
import com.eduquest.backend.infrastructure.persistence.submission.repository.SubmissionStatusJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JpaSubmissionCommandService implements SubmissionCommandService {

    private final SubmissionJpaRepository submissionJpaRepository;
    private final SubmissionStatusJpaRepository submissionStatusJpaRepository;
    private final SubmissionEntityMapper mapper;

    @Transactional
    @Override
    public Long saveSubmission(Submission submission) {
        SubmissionEntity entity = mapper.toEntity(submission);

        Long submissionId = submissionJpaRepository.save(entity).getId();

        SubmissionStatusEntity statusEntity = SubmissionStatusEntity.of(submissionId, SubmissionStatus.PENDING, 0);
        submissionStatusJpaRepository.save(statusEntity);

        return submissionId;
    }

    @Transactional
    @Override
    public void updateStatus(Long submissionId, SubmissionStatus status) {

        SubmissionStatusEntity statusEntity = submissionStatusJpaRepository.findBySubmissionId(submissionId)
                .orElseThrow(() -> new EduQuestException(SubmissionDatabaseErrorCode.SUBMISSION_STATUS_NOT_FOUND));

        statusEntity.changeStatus(status);
        submissionStatusJpaRepository.save(statusEntity);

    }

    @Transactional
    @Override
    public void updateRetryCount(Long submissionId) {

        SubmissionStatusEntity statusEntity = submissionStatusJpaRepository.findBySubmissionId(submissionId)
                .orElseThrow(() -> new EduQuestException(SubmissionDatabaseErrorCode.SUBMISSION_STATUS_NOT_FOUND));

        statusEntity.increaseTryCount();
        submissionStatusJpaRepository.save(statusEntity);

    }

}

