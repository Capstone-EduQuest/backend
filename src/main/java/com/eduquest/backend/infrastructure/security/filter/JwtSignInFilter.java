package com.eduquest.backend.infrastructure.security.filter;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.infrastructure.security.dto.SignInData;
import com.eduquest.backend.infrastructure.security.exception.SecurityErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtSignInFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // 로그인 시도 시, 사용자의 post body를 읽어서 Authentication 객체를 생성하여 반환
        // 예시에서는 username과 password를 JSON 형태로 받는다고 가정
        try {
            SignInData signInData = null;
            try {
                signInData = objectMapper.readValue(request.getInputStream(), SignInData.class);
            } catch (JacksonException e) {
                throw new EduQuestException(SecurityErrorCode.INVALID_TOKEN);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String username = signInData.id();
            String password = signInData.password();

            // Authentication 객체 생성
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            // AuthenticationManager를 사용하여 인증 시도
            return this.authenticationManager.authenticate(authenticationToken);

        } catch (Exception e) {
            log.error("Failed to parse authentication request body", e);
            throw new EduQuestException(SecurityErrorCode.INVALID_TOKEN);
        }

    }

}
