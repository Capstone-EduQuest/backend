package com.eduquest.backend.infrastructure.persistence.reward.service;

import com.eduquest.backend.domain.reward.model.Wallet;
import com.eduquest.backend.domain.reward.service.WalletQueryService;
import com.eduquest.backend.infrastructure.persistence.reward.mapper.WalletEntityMapper;
import com.eduquest.backend.infrastructure.persistence.reward.repository.WalletJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaWalletQueryService implements WalletQueryService {

    private final WalletJpaRepository walletRepository;
    private final WalletEntityMapper mapper;

    @Override
    public Wallet findByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .map(mapper::toDomain)
                .orElseGet(() -> Wallet.of(0L, userId));
    }
}

