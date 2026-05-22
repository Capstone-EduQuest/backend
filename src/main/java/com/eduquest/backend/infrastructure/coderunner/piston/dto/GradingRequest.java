package com.eduquest.backend.infrastructure.coderunner.piston.dto;

public record GradingRequest(Long submissionId) {

    public static GradingRequest fromSubmissionId(Long submissionId) {
        return new GradingRequest(submissionId);
    }

}

