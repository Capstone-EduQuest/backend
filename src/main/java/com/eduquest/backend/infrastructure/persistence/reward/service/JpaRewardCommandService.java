package com.eduquest.backend.infrastructure.persistence.reward.service;

import com.eduquest.backend.domain.reward.service.RewardCommandService;
import com.eduquest.backend.domain.reward.service.WalletCommandService;
import com.eduquest.backend.infrastructure.persistence.reward.entity.RewardHistoryEntity;
import com.eduquest.backend.infrastructure.persistence.reward.repository.RewardHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaRewardCommandService implements RewardCommandService {

    private final RewardHistoryJpaRepository rewardHistoryRepository;
    private final WalletCommandService walletCommandService;

    @Override
    @Transactional
    public void grantRewardIfNotExists(Long userId, Long stageId, Long amount, UUID stageUuid) {

        // idempotency check
        if (rewardHistoryRepository.existsByUserIdAndStageId(userId, stageId)) {
            return;
        }

        // Delegate wallet change & history persistence to WalletCommandService
        walletCommandService.changeBalance(userId, amount, "REWARD:" + stageUuid);

        // Persist reward history
        RewardHistoryEntity rh = RewardHistoryEntity.of(userId, stageId, amount);
        rewardHistoryRepository.save(rh);
    }
}

