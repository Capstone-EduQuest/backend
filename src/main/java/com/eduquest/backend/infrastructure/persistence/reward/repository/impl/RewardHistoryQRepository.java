package com.eduquest.backend.infrastructure.persistence.reward.repository.impl;

import com.eduquest.backend.domain.reward.model.RewardHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RewardHistoryQRepository {

    Page<RewardHistory> findByUserId(Long userId, Pageable pageable);

}
