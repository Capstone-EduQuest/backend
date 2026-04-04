package com.eduquest.backend.infrastructure.security.handler;

import com.eduquest.backend.infrastructure.security.repository.JwtRepository;
import com.eduquest.backend.infrastructure.security.util.JwtUtils;
import com.eduquest.backend.infrastructure.security.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtRepository jwtRepository;
    private final JwtUtils jwtUtils;
    private final TokenUtils tokenUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication) {

        if (authentication == null) {
            log.warn("No authentication information found during logout.");
        } else {
            // 인증된 사용자 정보에서 아이디 추출
            String userId = authentication.getName();
            log.info("Logging out user with ID: {}", userId);
        }

        // JWT 레지스트리에서 해당 사용자에 대한 JWT 정보 무효화
        jwtRepository.deleteByToken(tokenUtils.getRefreshTokenFromRequest(request));

        // 클라이언트에서 JWT 토큰 삭제
        response.addCookie(tokenUtils.emptyRefreshCookie());

    }
}
