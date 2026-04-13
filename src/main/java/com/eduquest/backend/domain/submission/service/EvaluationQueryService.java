package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.model.Evaluation;

import java.util.List;

public interface EvaluationQueryService {

    List<Evaluation> findBySubmissionIds(List<Long> submissionIds);

}

