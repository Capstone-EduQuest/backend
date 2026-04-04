package com.eduquest.backend.domain.member.service;

public interface MailService {

    void sendFindIdEmail(String recipientEmail);

    void sendResetPasswordEmail(String recipientEmail);

    boolean isValidToken(String token);

    String findEmailByToken(String token);

    void deleteToken(String token);

}
