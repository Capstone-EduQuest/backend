package com.eduquest.backend.domain.submission.event;

public record WrongNoteCreateRequestedEvent(
        Long submissionId,
        Long memberId,
        Long problemId,
        String wrongAnswer
) {
    public static WrongNoteCreateRequestedEvent of(Long submissionId, Long memberId, Long problemId, String wrongAnswer) {
        return new WrongNoteCreateRequestedEvent(submissionId, memberId, problemId, wrongAnswer);
    }
}

