package com.eduquest.backend.infrastructure.persistence.reward.service;

import com.eduquest.backend.domain.reward.model.RewardHistory;
import com.eduquest.backend.domain.reward.service.RewardQueryService;
import com.eduquest.backend.infrastructure.persistence.reward.mapper.RewardHistoryEntityMapper;
import com.eduquest.backend.infrastructure.persistence.reward.repository.RewardHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaRewardQueryService implements RewardQueryService {

    private final RewardHistoryJpaRepository rewardHistoryRepository;
    private final RewardHistoryEntityMapper mapper;

    @Override
    public List<RewardHistory> findRewardHistoryByUserIdByPagination(Long userId, int page, int size) {
        return rewardHistoryRepository.findByUserId(userId, Pageable.ofSize(size).withPage(page))
                .map(mapper::toDomain).stream().toList();
    }
}

