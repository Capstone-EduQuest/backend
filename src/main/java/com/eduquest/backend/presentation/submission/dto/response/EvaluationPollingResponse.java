package com.eduquest.backend.presentation.submission.dto.response;

public record EvaluationPollingResponse(String status, Boolean result) {

    public static EvaluationPollingResponse pending() {
        return new EvaluationPollingResponse("pending", null);
    }

    public static EvaluationPollingResponse completed(Boolean result) {
        return new EvaluationPollingResponse("completed", result);
    }

}

