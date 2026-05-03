package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.model.Submission;

import java.util.List;

public interface SubmissionQueryService {

    Submission findById(Long id);

    Submission findByUuid(java.util.UUID uuid);

    List<Submission> findByProblemId(Long problemId);

    List<Submission> findByUserId(Long userId);

}

