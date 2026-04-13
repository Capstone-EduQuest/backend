package com.eduquest.backend.presentation.progress.dto.response;

import lombok.Builder;

import java.util.List;

/**
 * 한 Stage에 대한 진행 정보
 * - stage: stage의 title
 * - totalQuestionCount: 해당 stage에 속한 problem의 총 개수
 * - clear: 사용자가 푼 problem의 number 리스트
 */
@Builder(access = lombok.AccessLevel.PROTECTED)
public record ProgressResponse(
        String stage,
        int totalQuestionCount,
        List<Integer> clear
) {

    public static ProgressResponse of(String stage, int totalQuestionCount, List<Integer> clear) {
        return ProgressResponse.builder()
                .stage(stage)
                .totalQuestionCount(totalQuestionCount)
                .clear(clear)
                .build();
    }

}

