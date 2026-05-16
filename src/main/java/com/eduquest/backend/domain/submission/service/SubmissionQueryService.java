package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.model.Submission;

import java.util.List;

public interface SubmissionQueryService {

    Submission findSubmissionById(Long id);

    Submission findSubmissionByUuid(java.util.UUID uuid);

    List<Submission> findSubmissionsByProblemId(Long problemId);

    List<Submission> findSubmissionsByUserId(Long userId);

}

