package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.model.Submission;

import java.util.List;

public interface SubmissionQueryService {

    Submission findById(Long id);

    List<Submission> findByProblemId(Long problemId);

}

