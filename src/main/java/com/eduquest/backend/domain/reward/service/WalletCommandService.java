package com.eduquest.backend.domain.reward.service;

public interface WalletCommandService {

    /**
     * wallet balance 를 증감하고 wallet_history 를 남긴다.
     * 트랜잭션 경계는 상위(application)에서 결정.
     *
     * @param userId 회원 PK
     * @param amount 증감량 (음수 = 차감)
     * @param reason 사유(예: "REWARD:<stageUuid>")
     */
    void changeBalance(Long userId, Long amount, String reason);
}

