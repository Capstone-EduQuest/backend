package com.eduquest.backend.domain.reward.service;

import com.eduquest.backend.domain.reward.model.RewardHistory;

import java.util.List;

public interface RewardQueryService {
    List<RewardHistory> findRewardHistoryByUserIdByPagination(Long userId, int page, int size);
}

