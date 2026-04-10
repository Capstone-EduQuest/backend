package com.eduquest.backend.domain.reward.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet {

    private Long id;
    private UUID uuid;
    private Long balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;

    @Builder(access = AccessLevel.PROTECTED)
    public Wallet(Long balance, Long userId) {
        this.balance = balance;
        this.userId = userId;
    }

    public static Wallet of(Long balance, Long userId) {
        return Wallet.builder()
                .balance(balance)
                .userId(userId)
                .build();
    }
}

