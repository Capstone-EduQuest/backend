package com.eduquest.backend.infrastructure.persistence.reward.service;

import com.eduquest.backend.domain.reward.service.WalletCommandService;
import com.eduquest.backend.infrastructure.persistence.reward.entity.WalletEntity;
import com.eduquest.backend.infrastructure.persistence.reward.entity.WalletHistoryEntity;
import com.eduquest.backend.infrastructure.persistence.reward.repository.WalletHistoryJpaRepository;
import com.eduquest.backend.infrastructure.persistence.reward.repository.WalletJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JpaWalletCommandService implements WalletCommandService {

    private final WalletJpaRepository walletRepository;
    private final WalletHistoryJpaRepository walletHistoryRepository;

    @Override
    @Transactional
    public void changeBalance(Long userId, Long amount, String reason) {

        WalletEntity wallet = walletRepository.findByUserIdForUpdate(userId)
                .orElseGet(() -> walletRepository.save(WalletEntity.of(userId, 0L)));

        if (amount >= 0) {
            wallet.increaseBalance(amount);
        } else {
            wallet.decreaseBalance(-amount);
        }

        walletRepository.save(wallet);

        WalletHistoryEntity history = WalletHistoryEntity.of(wallet.getId(), amount, reason);
        walletHistoryRepository.save(history);

    }

}

