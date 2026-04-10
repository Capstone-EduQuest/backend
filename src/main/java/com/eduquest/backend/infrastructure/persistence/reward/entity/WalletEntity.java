package com.eduquest.backend.infrastructure.persistence.reward.entity;

import com.eduquest.backend.infrastructure.persistence.common.entity.BasicUpdateEntity;
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
@Table(name = "wallet")
public class WalletEntity extends BasicUpdateEntity {

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder(access = AccessLevel.PROTECTED)
    public WalletEntity(Long balance, Long userId) {
        this.balance = balance;
        this.userId = userId;
    }

    public static WalletEntity of(Long userId, Long initialBalance) {
        return WalletEntity.builder()
                .userId(userId)
                .balance(initialBalance)
                .build();
    }

    public void increaseBalance(Long amount) {
        this.balance = this.balance + amount;
    }

    public void decreaseBalance(Long amount) {
        this.balance = this.balance - amount;
    }

}

