package com.eduquest.backend.domain.identity.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole {

    private Long id;
    private Long userId;
    private Long roleId;
    private LocalDateTime createdAt;

    @Builder(access = AccessLevel.PROTECTED)
    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public static UserRole of(Long userId, Long roleId) {
        return UserRole.builder()
                .userId(userId)
                .roleId(roleId)
                .build();
    }

}
