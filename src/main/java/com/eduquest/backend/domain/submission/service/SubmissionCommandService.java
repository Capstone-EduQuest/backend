package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.model.enums.SubmissionStatus;

public interface SubmissionCommandService {

	Long saveSubmission(Submission submission);

	void updateStatus(Long submissionId, SubmissionStatus status);

	void updateRetryCount(Long submissionId);

}


