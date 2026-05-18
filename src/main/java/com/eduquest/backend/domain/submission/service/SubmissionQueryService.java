package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.model.enums.SubmissionStatus;

import java.util.List;
import java.util.UUID;

public interface SubmissionQueryService {

    Submission findSubmissionById(Long id);

    Submission findSubmissionByUuid(UUID uuid);

    SubmissionStatus findSubmissionStatusBySubmissionId(Long submissionId);

    SubmissionStatus findSubmissionStatusBySubmissionUuid(UUID uuid);

    List<Submission> findSubmissionsByProblemId(Long problemId);

    List<Submission> findSubmissionsByUserId(Long userId);

}

