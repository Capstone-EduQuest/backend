package com.eduquest.backend.infrastructure.security.dto;

public record JwtToken(
        String accessToken
) {

    public static JwtToken of(String accessToken) {
        return new JwtToken(accessToken);
    }

}
