package com.eduquest.backend.domain.member.event;

public record AccountLockEvent(String userId) {
    public static AccountLockEvent of(String userId) {
        return new AccountLockEvent(userId);
    }
}
