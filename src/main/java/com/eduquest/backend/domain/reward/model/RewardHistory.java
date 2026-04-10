package com.eduquest.backend.domain.reward.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardHistory {

    private Long id;
    private UUID uuid;
    private Long userId;
    private Long stageId;
    private Long amount;
    private LocalDateTime createdAt;

    @Builder(access = AccessLevel.PROTECTED)
    public RewardHistory(Long userId, Long stageId, Long amount) {
        this.userId = userId;
        this.stageId = stageId;
        this.amount = amount;
    }

    public static RewardHistory of(Long userId, Long stageId, Long amount) {
        return RewardHistory.builder()
                .userId(userId)
                .stageId(stageId)
                .amount(amount)
                .build();
    }
}

