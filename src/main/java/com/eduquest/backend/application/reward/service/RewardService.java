package com.eduquest.backend.application.reward.service;

import com.eduquest.backend.application.reward.dto.RewardHistoryDto;
import com.eduquest.backend.application.reward.dto.RewardListDto;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import com.eduquest.backend.domain.reward.model.RewardHistory;
import com.eduquest.backend.domain.reward.service.RewardCommandService;
import com.eduquest.backend.domain.reward.service.RewardQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final MemberQueryService memberQueryService;
    private final StageQueryService stageQueryService;
    private final RewardCommandService rewardCommandService;
    private final RewardQueryService rewardQueryService;

    @Transactional
    public void handleStageCleared(UUID userUuid, UUID stageUuid) {
        Long userId = memberQueryService.findMemberIdByUuid(userUuid);
        Long stageId = stageQueryService.findIdByUuid(stageUuid);
        Long amount = stageQueryService.findRewardById(stageId);

        rewardCommandService.grantRewardIfNotExists(userId, stageId, amount, stageUuid);
    }

    public RewardListDto listRewards(UUID userUuid, int page, int size, String sort, Boolean isAsc) {
        Long userId = memberQueryService.findMemberIdByUuid(userUuid);

        Sort.Direction direction = (isAsc != null && isAsc) ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sort != null ? sort : "createdAt"));

        List<RewardHistory> rewardPage = rewardQueryService.findRewardHistoryByUserIdByPagination(userId, page, size);

        List<RewardHistoryDto> results = rewardPage.stream()
                .map(r -> RewardHistoryDto.of(r.getUuid(), r.getStageId(), r.getAmount(), r.getCreatedAt()))
                .toList();

        return RewardListDto.of(page, size, sort, isAsc, results);
    }
}

