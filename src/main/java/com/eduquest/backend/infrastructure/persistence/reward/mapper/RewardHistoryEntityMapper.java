package com.eduquest.backend.infrastructure.persistence.reward.mapper;

import com.eduquest.backend.domain.reward.model.RewardHistory;
import com.eduquest.backend.infrastructure.persistence.reward.entity.RewardHistoryEntity;
import org.springframework.stereotype.Component;

@Component
public class RewardHistoryEntityMapper {

    public RewardHistory toDomain(RewardHistoryEntity e) {
        return RewardHistory.of(e.getUserId(), e.getStageId(), e.getAmount());
    }

    public RewardHistoryEntity toEntity(RewardHistory d) {
        return RewardHistoryEntity.of(d.getUserId(), d.getStageId(), d.getAmount());
    }
}

