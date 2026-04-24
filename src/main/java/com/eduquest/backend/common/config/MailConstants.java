package com.eduquest.backend.common.config;

import java.time.Duration;

/**
 * Mail 관련 상수 정의
 */
public final class MailConstants {

    private MailConstants() {
        // util class
    }

    // 토큰 만료 시간 (분)
    public static final long TOKEN_EXPIRATION_MINUTES = 10L;

    // 토큰 만료 시간 Duration
    public static final Duration TOKEN_EXPIRATION = Duration.ofMinutes(TOKEN_EXPIRATION_MINUTES);

    // 토큰 만료 시간 밀리초 (스케줄러 등에서 사용)
    public static final long TOKEN_EXPIRATION_MILLIS = TOKEN_EXPIRATION.toMillis();

}

