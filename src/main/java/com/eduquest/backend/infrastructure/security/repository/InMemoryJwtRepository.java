package com.eduquest.backend.infrastructure.security.repository;

import com.eduquest.backend.infrastructure.security.dto.StoredToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 토큰을 인메모리에 저장하는 저장소
 * Refresh Token 블랙리스트 관리 및 유효성 검증을 위해 사용됩니다.
 */
@Slf4j
@Repository
public class InMemoryJwtRepository implements JwtRepository {

    /**
     * 토큰을 key로, StoredToken을 value로 저장하는 Map
     */
    private static final ConcurrentHashMap<String, StoredToken> TOKEN_STORE = new ConcurrentHashMap<>();

    /**
     * 토큰이 존재하고 만료되지 않았는지 확인
     *
     * @param token JWT 토큰
     * @return 유효한 토큰 여부
     */
    @Override
    public boolean existsByToken(String token) {
        StoredToken storedToken = TOKEN_STORE.get(token);
        
        if (storedToken == null) {
            return false;
        }
        
        // 만료된 토큰은 존재하지 않는 것으로 처리
        if (storedToken.isExpired()) {
            TOKEN_STORE.remove(token);
            return false;
        }
        
        return true;
    }

    /**
     * 토큰 조회
     *
     * @param token JWT 토큰
     * @return 저장된 토큰 정보 (만료되지 않은 경우)
     */
    @Override
    public Optional<String> findByToken(String token) {
        StoredToken storedToken = TOKEN_STORE.get(token);
        
        if (storedToken == null) {
            return Optional.empty();
        }
        
        // 만료된 토큰은 제거하고 반환하지 않음
        if (storedToken.isExpired()) {
            TOKEN_STORE.remove(token);
            return Optional.empty();
        }
        
        return Optional.of(token);
    }

    /**
     * 토큰 저장
     *
     * @param token JWT 토큰 (String으로 저장)
     */
    @Override
    public void save(String token) {
        // 문자열만 저장하는 기존 인터페이스 유지
        TOKEN_STORE.put(token, new StoredToken(
            token,
            null,  // userId는 다른 메소드에서 설정
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(14)  // 기본 14일 만료
        ));
    }

    /**
     * 토큰 저장 (사용자 정보 포함)
     *
     * @param token    JWT 토큰
     * @param userId   사용자 ID
     * @param expiresAt 토큰 만료 시간
     */
    @Override
    public void save(String token, String userId, LocalDateTime expiresAt) {
        StoredToken storedToken = new StoredToken(
            token,
            userId,
            LocalDateTime.now(),
            expiresAt
        );
        TOKEN_STORE.put(token, storedToken);
        log.debug("토큰 저장 - userId: {}, 만료시간: {}", userId, expiresAt);
    }

    /**
     * 토큰 삭제 (로그아웃 시 사용)
     *
     * @param token JWT 토큰
     */
    @Override
    public void deleteByToken(String token) {
        TOKEN_STORE.remove(token);
        log.debug("토큰 삭제: {}", token);
    }

    /**
     * 특정 사용자의 모든 토큰 삭제 (강제 로그아웃)
     *
     * @param userId 사용자 ID
     */
    public void deleteByUserId(Long userId) {
        TOKEN_STORE.entrySet().removeIf(entry -> 
            entry.getValue().getUserId() != null && 
            entry.getValue().getUserId().equals(userId)
        );
        log.debug("사용자의 모든 토큰 삭제 - userId: {}", userId);
    }

    /**
     * 특정 사용자의 토큰 개수 조회
     *
     * @param userId 사용자 ID
     * @return 활성 토큰 개수
     */
    public long countByUserId(Long userId) {
        return TOKEN_STORE.values().stream()
            .filter(token -> token.getUserId() != null && 
                           token.getUserId().equals(userId) && 
                           !token.isExpired())
            .count();
    }

    /**
     * 저장소 정보 조회
     *
     * @return 활성 토큰 개수
     */
    public int getStoreSize() {
        return TOKEN_STORE.size();
    }

    /**
     * 만료된 토큰을 정기적으로 정리 (매 1시간마다)
     */
    @Scheduled(fixedDelay = 3600000, initialDelay = 3600000)  // 1시간마다 실행
    public void cleanupExpiredTokens() {
        int beforeSize = TOKEN_STORE.size();
        
        TOKEN_STORE.entrySet().removeIf(entry -> entry.getValue().isExpired());
        
        int afterSize = TOKEN_STORE.size();
        int removedCount = beforeSize - afterSize;
        
        if (removedCount > 0) {
            log.info("만료된 토큰 정리 완료 - 제거된 토큰: {}개, 남은 토큰: {}개", removedCount, afterSize);
        }
    }
}
