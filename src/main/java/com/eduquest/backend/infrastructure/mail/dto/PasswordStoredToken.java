package com.eduquest.backend.infrastructure.mail.dto;

import java.time.LocalDateTime;

public record PasswordStoredToken(
        String email,
        LocalDateTime expiredAt
) {

    public static PasswordStoredToken of(String email, LocalDateTime expiredAt) {
        return new PasswordStoredToken(email, expiredAt);
    }

}
