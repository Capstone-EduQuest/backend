package com.eduquest.backend.infrastructure.mail.repository;

import com.eduquest.backend.infrastructure.mail.dto.PasswordStoredToken;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryEmailTokenRepository implements EmailTokenRepository {

    private static final ConcurrentHashMap<String, PasswordStoredToken> PASSWORD_RESET_TOKEN_STORE = new ConcurrentHashMap<>();

    @Override
    public boolean existsByToken(String token) {
        return PASSWORD_RESET_TOKEN_STORE.containsKey(token);
    }

    @Override
    public void save(String token, String email) {

        if (!existsByEmail(email)) {
            PASSWORD_RESET_TOKEN_STORE.put(token, PasswordStoredToken.of(email, LocalDateTime.now()));
        }

    }

    @Override
    public void deleteByToken(String token) {
        PASSWORD_RESET_TOKEN_STORE.remove(token);
    }

    @Override
    public String findEmailByToken(String token) {
        return PASSWORD_RESET_TOKEN_STORE.get(token).email();
    }

    @Override
    public boolean existsByEmail(String email) {
        return PASSWORD_RESET_TOKEN_STORE.values().stream()
                .anyMatch(storedToken -> storedToken.email().equals(email));
    }

    @Scheduled(fixedDelay = 1000 * 60 * 10, initialDelay = 1000 * 60 * 10)  // 10분마다 실행
    public void cleanupExpiredTokens() {
        int beforeSize = PASSWORD_RESET_TOKEN_STORE.size();

        // 토큰이 생성된 지 10분이 지난 경우 삭제
        PASSWORD_RESET_TOKEN_STORE.entrySet().removeIf(entry -> {
            LocalDateTime tokenCreationTime = entry.getValue().expiredAt();
            return tokenCreationTime.plusMinutes(10).isBefore(LocalDateTime.now());
        });
    }

}
