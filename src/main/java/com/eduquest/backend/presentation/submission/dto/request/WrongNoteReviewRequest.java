package com.eduquest.backend.presentation.submission.dto.request;

import jakarta.validation.constraints.NotNull;

public record WrongNoteReviewRequest(
        @NotNull(message = "isReviewed는 필수입니다.") Boolean isReviewed
) {

    public static WrongNoteReviewRequest of(Boolean isReviewed) {
        return new WrongNoteReviewRequest(isReviewed);
    }

}

