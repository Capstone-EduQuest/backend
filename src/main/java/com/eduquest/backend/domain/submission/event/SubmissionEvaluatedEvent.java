package com.eduquest.backend.domain.submission.event;

import java.util.UUID;

public record SubmissionEvaluatedEvent(
        Long submissionId,
        Long memberId,
        Boolean isCorrect,
        UUID stageUuid,
        String problemType
) {
    public static SubmissionEvaluatedEvent of(Long submissionId, Long memberId, Boolean isCorrect, UUID stageUuid, String problemType) {
        return new SubmissionEvaluatedEvent(submissionId, memberId, isCorrect, stageUuid, problemType);
    }
}

