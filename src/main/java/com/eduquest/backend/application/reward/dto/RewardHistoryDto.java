package com.eduquest.backend.application.reward.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RewardHistoryDto(
        UUID uuid,
        Long stageId,
        Long amount,
        LocalDateTime createdAt
) {

    public static RewardHistoryDto of(UUID uuid, Long stageId, Long amount, LocalDateTime createdAt) {
        return new RewardHistoryDto(uuid, stageId, amount, createdAt);
    }
}

