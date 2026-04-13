package com.eduquest.backend.presentation.progress.dto.response;

import java.util.List;

/**
 * 사용자 진행 목록 응답
 * {
 *   "results": [ { "stage": "...", "totalQuestionCount": 10, "clear": [1,2,3] }, ... ]
 * }
 */
public record ProgressListResponse(
        List<ProgressResponse> results
) {

    public static ProgressListResponse of(List<ProgressResponse> results) {
        return new ProgressListResponse(results);
    }

}

