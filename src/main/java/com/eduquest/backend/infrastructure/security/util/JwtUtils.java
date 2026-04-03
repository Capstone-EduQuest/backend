package com.eduquest.backend.infrastructure.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret_key}")
    private String jwtSecret;

    @Value("${jwt.access_key_expiration}")
    private long accessKeyExpiration;

    @Value("${jwt.refresh_key_expiration}")
    private long refreshKeyExpiration;

    public long getAccessTokenExpiration() {
        return accessKeyExpiration;
    }

    public long getRefreshTokenExpiration() {
        return refreshKeyExpiration;
    }

    /**
     * 서명 키 생성
     *
     * @return SecretKey
     */
    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Access Token 생성
     *
     * @param userId 사용자 ID
     * @param role   사용자 역할
     * @return 생성된 Access Token
     */
    public String generateAccessToken(String userId, String role) {
        return generateToken(userId, role, accessKeyExpiration);
    }

    /**
     * Refresh Token 생성
     *
     * @param userId 사용자 ID
     * @param role   사용자 역할
     * @return 생성된 Refresh Token
     */
    public String generateRefreshToken(String userId, String role) {
        return generateToken(userId, role, refreshKeyExpiration);
    }

    /**
     * Token 생성
     *
     * @param userId     사용자 ID
     * @param role       사용자 역할
     * @param expiration 토큰 만료 시간(밀리초)
     * @return 생성된 Token
     */
    private String generateToken(String userId, String role, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * Token에서 userId 추출
     *
     * @param token JWT Token
     * @return userId
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * Token에서 role 추출
     *
     * @param token JWT Token
     * @return role
     */
    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    /**
     * Token의 만료 시간 추출
     *
     * @param token JWT Token
     * @return 만료 시간
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * Token이 만료되었는지 확인
     *
     * @param token JWT Token
     * @return 만료 여부
     */
    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Token의 유효성 검증
     *
     * @param token JWT Token
     * @return 유효 여부
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .keyLocator(cmd -> getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return !isTokenExpired(token);
        } catch (SignatureException e) {
            log.error("유효하지 않은 JWT 서명: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("유효하지 않은 JWT 토큰: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT 토큰: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT 클레임이 비어있음: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Token에서 모든 Claims 추출
     *
     * @param token JWT Token
     * @return Claims
     */
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .keyLocator(cmd -> getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
