package com.eduquest.backend.infrastructure.persistence.reward.repository;

import com.eduquest.backend.infrastructure.persistence.reward.entity.RewardHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardHistoryJpaRepository extends JpaRepository<RewardHistoryEntity, Long> {

    boolean existsByUserIdAndStageId(Long userId, Long stageId);

    Page<RewardHistoryEntity> findByUserId(Long userId, Pageable pageable);

}

