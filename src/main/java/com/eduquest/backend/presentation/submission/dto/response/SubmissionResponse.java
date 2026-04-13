package com.eduquest.backend.presentation.submission.dto.response;

public record SubmissionResponse(
        boolean result
) {

    public static SubmissionResponse ok(boolean result) {
        return new SubmissionResponse(result);
    }

}

