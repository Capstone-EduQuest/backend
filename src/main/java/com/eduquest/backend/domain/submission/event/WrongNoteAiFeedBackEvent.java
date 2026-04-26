package com.eduquest.backend.domain.submission.event;

import java.util.UUID;

public record WrongNoteAiFeedBackEvent(
        UUID wrongNoteUuid,
        String summary,
        String expectedOutput,
        String block,
        String wrongAnswer
) {
}
