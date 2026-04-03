package com.eduquest.backend.infrastructure.security.dto;

public record JwtToken(
        String accessToken,
        String refreshToken
) {

    public static JwtToken of(String accessToken, String refreshToken) {
        return new JwtToken(accessToken, refreshToken);
    }

}
