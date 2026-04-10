package com.eduquest.backend.domain.reward.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletHistory {

    private Long id;
    private UUID uuid;
    private Long amount;
    private String reason;
    private Long walletId;
    private LocalDateTime createdAt;

    @Builder(access = AccessLevel.PROTECTED)
    public WalletHistory(Long walletId, Long amount, String reason) {
        this.walletId = walletId;
        this.amount = amount;
        this.reason = reason;
    }

    public static WalletHistory of(Long walletId, Long amount, String reason) {
        return WalletHistory.builder()
                .walletId(walletId)
                .amount(amount)
                .reason(reason)
                .build();
    }
}

