package com.eduquest.backend.domain.member.event;

import jakarta.servlet.http.HttpServletResponse;

public record RotateTokenEvent(String refreshToken, HttpServletResponse response) {

    public static RotateTokenEvent of(String refreshToken, HttpServletResponse response) {
        return new RotateTokenEvent(refreshToken, response);
    }

}
