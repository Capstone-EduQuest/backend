package com.eduquest.backend.infrastructure.security.config;

import com.eduquest.backend.infrastructure.security.filter.JwtAuthenticationFilter;
import com.eduquest.backend.infrastructure.security.filter.JwtSignInFilter;
import com.eduquest.backend.infrastructure.security.handler.JwtLoginFailureHandler;
import com.eduquest.backend.infrastructure.security.handler.JwtLoginSuccessHandler;
import com.eduquest.backend.infrastructure.security.handler.JwtLogoutHandler;
import com.eduquest.backend.infrastructure.security.handler.JwtLogoutSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtLoginSuccessHandler jwtLoginSuccessHandler;
    private final JwtLoginFailureHandler jwtLoginFailureHandler;
    private final JwtLogoutHandler jwtLogoutHandler;
    private final JwtLogoutSuccessHandler jwtLogoutSuccessHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(AbstractHttpConfigurer::disable)
                .logout(logout -> {
                    logout.logoutUrl("/api/v1/auth/logout");
                    logout.addLogoutHandler(jwtLogoutHandler);
                    logout.logoutSuccessHandler(jwtLogoutSuccessHandler);
                })
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/v1/sign-up",
                        "/api/v1/auth/sign-in",
                        "/api/v1/auth/find-id",
                        "/api/v1/auth/find-password",
                        "/api/v1/auth/reset-password",
                        "/api/v1/auth/refresh"

                )
                    .permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
            )
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // UsernamePasswordAuthenticationFilter 위치에 로그인 처리용 필터 추가
        JwtSignInFilter jwtSignInFilter = new JwtSignInFilter(objectMapper, authenticationManager);
        // 컨트롤러의 로그인 엔드포인트와 일치시킴
        jwtSignInFilter.setFilterProcessesUrl("/api/v1/auth/sign-in");
        // 성공/실패 핸들러 연결
        jwtSignInFilter.setAuthenticationSuccessHandler(jwtLoginSuccessHandler);
        jwtSignInFilter.setAuthenticationFailureHandler(jwtLoginFailureHandler);

        http.addFilterAt(jwtSignInFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontendUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
