package com.eduquest.backend.infrastructure.mail.repository;

import com.eduquest.backend.infrastructure.mail.dto.PasswordStoredToken;
import com.eduquest.backend.common.config.MailConstants;
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

    @Scheduled(fixedDelayString = "${app.mail.token-expiration-millis:600000}", initialDelayString = "${app.mail.token-expiration-millis:600000}")
    public void cleanupExpiredTokens() {

        // 토큰이 생성된 지 TOKEN_EXPIRATION(기본 10분)이 지난 경우 삭제
        PASSWORD_RESET_TOKEN_STORE.entrySet().removeIf(entry -> {
            LocalDateTime tokenCreationTime = entry.getValue().expiredAt();
            return tokenCreationTime.plus(MailConstants.TOKEN_EXPIRATION).isBefore(LocalDateTime.now());
        });
    }

}
