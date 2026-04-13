package com.eduquest.backend.presentation.submission.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SubmissionRequest(
        @NotBlank(message = "답안은 필수 입니다.") String answer
) {

}

