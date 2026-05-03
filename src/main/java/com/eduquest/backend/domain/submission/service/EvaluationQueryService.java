package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.model.Evaluation;

import java.util.List;
import java.util.UUID;

public interface EvaluationQueryService {

    Evaluation findBySubmissionId(Long submissionId);

    List<Evaluation> findBySubmissionIds(List<Long> submissionIds);

}

