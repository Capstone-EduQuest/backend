package com.eduquest.backend.domain.progress.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgressQuery {

    /**
     * 스테이지별 진행(요약) 정보를 담는 조회용 DTO
     *
     * 필드:
     *  - stageId: DB PK (Long)
     *  - stageUuid: stage UUID
     *  - stageTitle: stage.title
     *  - stageNumber: stage.number
     *  - totalQuestionCount: 해당 스테이지의 문제 개수
     */
    public record Detail(
            Long stageId,
            UUID stageUuid,
            String stageTitle,
            Integer stageNumber,
            Long totalQuestionCount
    ) {
        public static Detail of(Long stageId, UUID stageUuid, String stageTitle, Integer stageNumber, Long totalQuestionCount) {
            return new Detail(stageId, stageUuid, stageTitle, stageNumber, totalQuestionCount);
        }
    }

}

