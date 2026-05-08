package com.eduquest.backend.domain.learning.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class StageQuery {

    public record Summary(Long stageId, UUID stageUuid, String title, Integer number, Long reward) {
        public static Summary of(Long stageId, UUID stageUuid, String title, Integer number, Long reward) {
            return new Summary(stageId, stageUuid, title, number, reward);
        }
    }

}

