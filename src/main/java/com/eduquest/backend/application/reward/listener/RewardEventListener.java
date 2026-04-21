package com.eduquest.backend.application.reward.listener;

import com.eduquest.backend.application.reward.service.RewardService;
import com.eduquest.backend.domain.learning.event.StageClearedEvent;
import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import com.eduquest.backend.domain.reward.event.GrantPointEvent;
import com.eduquest.backend.domain.reward.model.WalletHistory;
import com.eduquest.backend.domain.reward.service.WalletCommandService;
import com.eduquest.backend.domain.reward.service.WalletQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RewardEventListener {

    private final RewardService rewardService;
    private final MemberQueryService memberQueryService;
    private final WalletCommandService walletCommandService;
    private final WalletQueryService walletQueryService;

    @Transactional
    @EventListener
    public void handleGrantPointEvent(GrantPointEvent event) {

        // 존재하는 멤버인지 검사
        Member member = memberQueryService.findMemberById(event.memberId());

        walletCommandService.changeBalance(
                member.getId(),
                event.point(),
                event.reason()
        );

        Long walletId = walletQueryService.findByUserId(member.getId()).getId();

        walletCommandService.saveWalletHistory(
                WalletHistory.of(walletId, event.point(), event.reason())
        );

    }

    @EventListener
    public void handleStageCleared(StageClearedEvent event) {
        rewardService.handleStageCleared(event.userUuid(), event.stageUuid());
    }
}

