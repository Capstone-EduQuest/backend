package com.eduquest.backend.infrastructure.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 저장된 JWT 토큰 정보
 */
@Getter
@AllArgsConstructor
public class StoredToken implements Serializable {

    private static final long serialVersionUID = 1L;
    private String token;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

}

