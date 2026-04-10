package com.eduquest.backend.infrastructure.persistence.reward.mapper;

import com.eduquest.backend.domain.reward.model.WalletHistory;
import com.eduquest.backend.infrastructure.persistence.reward.entity.WalletHistoryEntity;
import org.springframework.stereotype.Component;

@Component
public class WalletHistoryEntityMapper {

    public WalletHistory toDomain(WalletHistoryEntity e) {
        return WalletHistory.of(e.getWalletId(), e.getAmount(), e.getReason());
    }

    public WalletHistoryEntity toEntity(WalletHistory d) {
        return WalletHistoryEntity.of(d.getWalletId(), d.getAmount(), d.getReason());
    }
}

