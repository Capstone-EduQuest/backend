package com.eduquest.backend.infrastructure.security.listener;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.member.event.RotateTokenEvent;
import com.eduquest.backend.infrastructure.security.dto.JwtToken;
import com.eduquest.backend.infrastructure.security.exception.SecurityErrorCode;
import com.eduquest.backend.infrastructure.security.repository.JwtRepository;
import com.eduquest.backend.infrastructure.security.util.JwtUtils;
import com.eduquest.backend.infrastructure.security.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenEventListener {

    private final JwtRepository jwtRepository;
    private final JwtUtils jwtUtils;
    private final TokenUtils tokenUtils;
    private final ObjectMapper objectMapper;

    @EventListener
    public void handleRotateTokenEvent(RotateTokenEvent event) {

        // 존재하는 refresh token인지 확인
        if (!jwtRepository.existsByToken(event.refreshToken())) {
            throw new EduQuestException(SecurityErrorCode.INVALID_TOKEN);
        }

        // refresh token 삭제
        jwtRepository.deleteByToken(event.refreshToken());

        // token 재발급
        String username = jwtUtils.getUserIdFromToken(event.refreshToken());
        String role = jwtUtils.getRoleFromToken(event.refreshToken());
        String newAccessToken = jwtUtils.generateAccessToken(username, role);
        String newRefreshToken = jwtUtils.generateRefreshToken(username, role);

        // 토큰 갱신
        // JWT refresh 토큰 저장소에 저장
        // JwtUtils.getRefreshTokenExpiration() returns milliseconds; convert to seconds for LocalDateTime.plusSeconds
        long refreshExpiryMillis = jwtUtils.getRefreshTokenExpiration();
        jwtRepository.save(newRefreshToken, username, LocalDateTime.now().plusSeconds(refreshExpiryMillis / 1000));

        // accessToken을 Json 형태로 응답 본문에 작성
        event.response().setContentType("application/json");
        event.response().setCharacterEncoding("UTF-8");
        try {
            // accessToken(key) : token(value)
            objectMapper.writeValue(event.response().getWriter(), JwtToken.of(newAccessToken));
        } catch (IOException e) {
            log.error("Failed to write access token to response body", e);
            throw new RuntimeException(e);
        }

        // HttpOnly, Secure, SameSite 설정이 적용된 쿠키 생성
        event.response().addCookie(tokenUtils.createRefreshTokenCookie(newRefreshToken, jwtUtils.getRefreshTokenExpiration()));

    }

}
