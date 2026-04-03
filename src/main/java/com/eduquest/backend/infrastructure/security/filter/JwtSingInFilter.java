package com.eduquest.backend.infrastructure.security.filter;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.infrastructure.security.dto.SignInData;
import com.eduquest.backend.infrastructure.security.exception.SecurityErrorCode;
import com.eduquest.backend.infrastructure.security.repository.JwtRepository;
import com.eduquest.backend.infrastructure.security.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtSingInFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtils jwtUtils;
    private final JwtRepository jwtRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // 로그인 시도 시, 사용자의 post body를 읽어서 Authentication 객체를 생성하여 반환
        // 예시에서는 username과 password를 JSON 형태로 받는다고 가정
        try {
            SignInData signInData = null;
            try {
                signInData = new ObjectMapper().readValue(request.getInputStream(), SignInData.class);
            } catch (JacksonException e) {
                throw new EduQuestException(SecurityErrorCode.USER_NOT_FOUND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String username = signInData.id();
            String password = signInData.password();

            // Authentication 객체 생성
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            // AuthenticationManager를 사용하여 인증 시도
            return authenticationManager.authenticate(authenticationToken);

        } catch (Exception e) {
            log.error("Failed to parse authentication request body", e);
            throw new EduQuestException(SecurityErrorCode.USER_NOT_FOUND);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {
        // 인증 성공 시, JWT 토큰을 생성하여 응답 헤더에 추가하는 로직을 구현
        // 예시에서는 JWT 토큰 생성 로직이 생략되어 있습니다.
        // 실제 구현에서는 JWT 토큰을 생성하여 response.addHeader("Authorization", "Bearer " + token) 형태로 추가해야 합니다.
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");
        String accessToken = jwtUtils.generateAccessToken(username, role);
        String refreshToken = jwtUtils.generateRefreshToken(username, role);

        // JWT refresh 토큰 저장소에 저장
        jwtRepository.save(refreshToken);

        // HttpOnly, Secure, SameSite 설정이 적용된 쿠키 생성
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(jwtUtils.getRefreshTokenExpiration())
                .build();

        // 쿠키를 응답 헤더에 추가
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        response.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {
        // 인증 실패 시, 적절한 에러 응답을 반환하는 로직을 구현
        // 예시에서는 인증 실패 시 401 Unauthorized 상태 코드를 반환하도록 설정되어 있습니다.
        log.error("Authentication failed: {}", failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
