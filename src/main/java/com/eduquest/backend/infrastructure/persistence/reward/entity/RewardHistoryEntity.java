package com.eduquest.backend.infrastructure.persistence.reward.entity;

import com.eduquest.backend.infrastructure.persistence.common.entity.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reward_history")
public class RewardHistoryEntity extends BasicEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "stage_id", nullable = false)
    private Long stageId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Builder(access = AccessLevel.PROTECTED)
    public RewardHistoryEntity(Long userId, Long stageId, Long amount) {
        this.userId = userId;
        this.stageId = stageId;
        this.amount = amount;
    }

    public static RewardHistoryEntity of(Long userId, Long stageId, Long amount) {
        return RewardHistoryEntity.builder()
                .userId(userId)
                .stageId(stageId)
                .amount(amount)
                .build();
    }
}

