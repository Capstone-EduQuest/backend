package com.eduquest.backend.infrastructure.security.filter;

import com.eduquest.backend.infrastructure.security.repository.JwtRepository;
import com.eduquest.backend.infrastructure.security.service.CustomUserDetailsService;
import com.eduquest.backend.infrastructure.security.util.JwtUtils;
import com.eduquest.backend.infrastructure.security.util.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final TokenUtils tokenUtils;
    private final JwtRepository jwtRepository;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            // 헤더에서 JWT 토큰 추출
            String token = tokenUtils.getAccessTokenFromRequest(request);

            if (token != null && !token.isBlank()) {
                // 유효한 서명/만료인지 검사
                boolean valid = jwtUtils.validateToken(token);
                boolean isExpired = jwtUtils.isTokenExpired(token);
                if (valid && !isExpired) {
                    // 토큰에서 userId(subject)를 추출 (프로젝트 JwtUtils는 String 반환)
                    String userId = jwtUtils.getUserIdFromToken(token);

                    // CustomUserDetailsService는 userId 문자열로 조회하도록 구현되어 있으므로 String.valueOf 사용
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    String uuidStr = null;

                    // uuid를 authentication에 추가
                    try {
                        uuidStr = jwtUtils.getUuidFromToken(token);
                    } catch (Exception e) {
                        if (uuidStr == null && !uuidStr.isBlank()) {
                            authentication.setDetails(uuidStr);
                        }
                    }

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else if (isExpired) {
                    log.error("Expired JWT token for request: {}", request.getRequestURI());

                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"JWT token has expired\"}");
                    return;
                }
                else {
                    log.error("Invalid JWT token for request: {}", request.getRequestURI());
                }
            }
        } catch (Exception ex) {
            // 파싱/검증/유저조회 중 예외 발생 시 SecurityContext를 비우고 다음 필터로 넘김
            log.error("Failed to set user authentication from JWT: {}", ex.getMessage());
            SecurityContextHolder.clearContext();

        }

        filterChain.doFilter(request, response);

    }

}
