package com.eduquest.backend.infrastructure.mail.repository;

public interface PasswordResetTokenRepository {

    boolean existsByToken(String token);

    boolean existsByEmail(String email);

    void save(String token, String email);

    void deleteByToken(String token);

}
