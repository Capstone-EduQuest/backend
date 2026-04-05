package com.eduquest.backend.infrastructure.security.config;

import com.eduquest.backend.infrastructure.security.filter.JwtAuthenticationFilter;
import com.eduquest.backend.infrastructure.security.filter.JwtSingInFilter;
import com.eduquest.backend.infrastructure.security.handler.JwtLoginFailureHandler;
import com.eduquest.backend.infrastructure.security.handler.JwtLoginSuccessHandler;
import com.eduquest.backend.infrastructure.security.handler.JwtLogoutHandler;
import com.eduquest.backend.infrastructure.security.handler.JwtLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtLoginSuccessHandler jwtLoginSuccessHandler;
    private final JwtLoginFailureHandler jwtLoginFailureHandler;
    private final JwtLogoutHandler jwtLogoutHandler;
    private final JwtLogoutSuccessHandler jwtLogoutSuccessHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(AbstractHttpConfigurer::disable)
                .logout(logout -> {
                    logout.addLogoutHandler(jwtLogoutHandler);
                    logout.logoutSuccessHandler(jwtLogoutSuccessHandler);
                })
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/v1/sign-up",
                        "/api/v1/auth/sign-in",
                        "/api/v1/auth/find-userId",
                        "/api/v1/auth/find-password",
                        "/api/v1/auth/reset-password",
                        "/api/v1/auth/refresh"

                )
                    .permitAll()
                .anyRequest().authenticated()
            )
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(
                        new JwtSingInFilter(objectMapper, authenticationManager),
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

}
