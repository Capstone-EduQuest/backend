package com.eduquest.backend.domain.reward.service;

import java.util.UUID;

public interface RewardCommandService {

    /**
     * 주어진 memberId, stageId 에 대해 보상이 이미 지급되어 있지 않다면 지급(기록 및 wallet 갱신 포함).
     * 구현은 트랜잭션 내부에서 idempotency 검사 및 wallet 잠금을 수행해야 함.
     *
     * @param userId  회원 PK
     * @param stageId 스테이지 PK
     * @param amount  지급할 금액
     * @param stageUuid 스테이지 UUID (로그용)
     */
    void grantRewardIfNotExists(Long userId, Long stageId, Long amount, UUID stageUuid);
}

