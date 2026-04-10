package com.eduquest.backend.infrastructure.persistence.reward.mapper;

import com.eduquest.backend.domain.reward.model.Wallet;
import com.eduquest.backend.infrastructure.persistence.reward.entity.WalletEntity;
import org.springframework.stereotype.Component;

@Component
public class WalletEntityMapper {

    public Wallet toDomain(WalletEntity e) {
        return Wallet.of(e.getBalance(), e.getUserId());
    }

    public WalletEntity toEntity(Wallet d) {
        return WalletEntity.of(d.getUserId(), d.getBalance());
    }
}

