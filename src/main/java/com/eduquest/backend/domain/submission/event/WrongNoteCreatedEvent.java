package com.eduquest.backend.domain.submission.event;

public record WrongNoteCreatedEvent(
        Long wrongNoteId,
        Long userId,
        Long problemId
) {
    public static WrongNoteCreatedEvent of(Long wrongNoteId, Long userId, Long problemId) {
        return new WrongNoteCreatedEvent(wrongNoteId, userId, problemId);
    }
}

