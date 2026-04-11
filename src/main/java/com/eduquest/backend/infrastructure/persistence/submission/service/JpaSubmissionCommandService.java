package com.eduquest.backend.infrastructure.persistence.submission.service;

import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.service.SubmissionCommandService;
import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionEntity;
import com.eduquest.backend.infrastructure.persistence.submission.mapper.SubmissionEntityMapper;
import com.eduquest.backend.infrastructure.persistence.submission.repository.SubmissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaSubmissionCommandService implements SubmissionCommandService {

    private final SubmissionJpaRepository submissionJpaRepository;
    private final SubmissionEntityMapper mapper;

    @Override
    public Long saveSubmission(Submission submission) {
        SubmissionEntity entity = mapper.toEntity(submission);
        return submissionJpaRepository.save(entity).getId();
    }

}

