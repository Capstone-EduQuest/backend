package com.eduquest.backend.infrastructure.security.repository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JwtRepository {

    boolean existsByToken(String token);

    Optional<String> findByToken(String token);

    void save(String token);

    void save(String token, String userId, LocalDateTime expiresAt);

    void deleteByToken(String token);

}
