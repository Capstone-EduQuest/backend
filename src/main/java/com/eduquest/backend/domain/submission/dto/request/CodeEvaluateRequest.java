package com.eduquest.backend.domain.submission.dto.request;

public record CodeEvaluateRequest(
        String source,
        String language,
        String input,
        Long timeLimitMs,
        Long memoryLimitKb,
        Boolean compileOnly
) {

    public CodeEvaluateRequest {

        if (timeLimitMs == null) {
            timeLimitMs = 0L;
        }
        if (memoryLimitKb == null) {
            memoryLimitKb = 0L;
        }
        if (compileOnly == null) {
            compileOnly = Boolean.FALSE;
        }
    }

}
