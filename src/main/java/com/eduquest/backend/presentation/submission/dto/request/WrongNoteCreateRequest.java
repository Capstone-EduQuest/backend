package com.eduquest.backend.presentation.submission.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WrongNoteCreateRequest(
        @NotNull(message = "problemId는 필수입니다.") Long problemId,
        @NotBlank(message = "wrongAnswer는 필수입니다.")
        @Size(max = 2000, message = "wrongAnswer는 최대 2000자 입니다.")
        String wrongAnswer,
        @Size(max = 2000, message = "feedback은 최대 2000자 입니다.")
        String feedback
) {

    public static WrongNoteCreateRequest of(Long problemId, String wrongAnswer, String feedback) {
        return new WrongNoteCreateRequest(problemId, wrongAnswer, feedback);
    }

}

