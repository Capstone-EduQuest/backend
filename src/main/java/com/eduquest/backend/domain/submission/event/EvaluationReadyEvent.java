package com.eduquest.backend.domain.submission.event;

import java.util.UUID;

public record EvaluationReadyEvent(Object source, UUID submissionUuid) {

	public static EvaluationReadyEvent of(Object source, UUID submissionUuid) {
		return new EvaluationReadyEvent(source, submissionUuid);
	}

}
