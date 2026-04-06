package com.eduquest.backend.infrastructure.security.handler;

import com.eduquest.backend.infrastructure.security.dto.JwtToken;
import com.eduquest.backend.infrastructure.security.repository.JwtRepository;
import com.eduquest.backend.infrastructure.security.util.JwtUtils;
import com.eduquest.backend.infrastructure.security.util.TokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final TokenUtils tokenUtils;
    private final ObjectMapper objectMapper;
    private final JwtRepository jwtRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");
        String accessToken = jwtUtils.generateAccessToken(username, role);
        String refreshToken = jwtUtils.generateRefreshToken(username, role);

        // JWT refresh 토큰 저장소에 저장
        // JwtUtils.getRefreshTokenExpiration() returns milliseconds; convert to seconds for LocalDateTime.plusSeconds
        long refreshExpiryMillis = jwtUtils.getRefreshTokenExpiration();
        jwtRepository.save(refreshToken, username, LocalDateTime.now().plusSeconds(refreshExpiryMillis / 1000));

        // accessToken을 Json 형태로 응답 본문에 작성
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            // accessToken(key) : token(value)
            objectMapper.writeValue(response.getWriter(), JwtToken.of(accessToken));
        } catch (IOException e) {
            log.error("Failed to write access token to response body", e);
            throw new RuntimeException(e);
        }

        // HttpOnly, Secure, SameSite 설정이 적용된 쿠키 생성
        response.addCookie(tokenUtils.createRefreshTokenCookie(refreshToken, jwtUtils.getRefreshTokenExpiration()));

    }
}
