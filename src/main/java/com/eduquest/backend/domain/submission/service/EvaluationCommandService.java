package com.eduquest.backend.domain.submission.service;


public interface EvaluationCommandService {

    Long saveEvaluation(Boolean isCorrect, Long submissionId);
}

