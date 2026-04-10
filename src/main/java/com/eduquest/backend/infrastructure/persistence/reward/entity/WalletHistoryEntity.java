package com.eduquest.backend.infrastructure.persistence.reward.entity;

import com.eduquest.backend.infrastructure.persistence.common.entity.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "wallet_history")
public class WalletHistoryEntity extends BasicEntity {

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "wallet_id", nullable = false)
    private Long walletId;

    @Builder(access = AccessLevel.PROTECTED)
    public WalletHistoryEntity(Long amount, String reason, Long walletId) {
        this.amount = amount;
        this.reason = reason;
        this.walletId = walletId;
    }

    public static WalletHistoryEntity of(Long walletId, Long amount, String reason) {
        return WalletHistoryEntity.builder()
                .walletId(walletId)
                .amount(amount)
                .reason(reason)
                .build();
    }
}

