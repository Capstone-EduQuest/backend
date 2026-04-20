package com.eduquest.backend.domain.submission.dto.request;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public record CodeEvaluateRequest(
        String source,
        String language,
        String version,
        String fileName,
        String input,
        Long compileTimeLimitMs,
        Long compileTimeMemoryLimitKb,
        Long runTimeLimitMs,
        Long runtTimeMemoryLimitKb,
        Boolean compileOnly
) {

    public static CodeEvaluateRequest of(
            String source,
            String language,
            String version,
            String fileName,
            String input,
            Long compileTimeLimitMs,
            Long compileTimeMemoryLimitKb,
            Long runTimeLimitMs,
            Long runtTimeMemoryLimitKb,
            Boolean compileOnly
    ) {

        return CodeEvaluateRequest.builder()
                .source(source)
                .language(language)
                .version(version)
                .fileName(fileName)
                .input(input)
                .compileTimeLimitMs(compileTimeLimitMs)
                .compileTimeMemoryLimitKb(compileTimeMemoryLimitKb)
                .runTimeLimitMs(runTimeLimitMs)
                .runtTimeMemoryLimitKb(runtTimeMemoryLimitKb)
                .compileOnly(compileOnly)
                .build();

    }

    public CodeEvaluateRequest {

        if (runTimeLimitMs == null) {
            runTimeLimitMs = 0L;
        }
        if (runtTimeMemoryLimitKb == null) {
            runtTimeMemoryLimitKb = 0L;
        }
        if (compileOnly == null) {
            compileOnly = Boolean.FALSE;
        }
    }

}
