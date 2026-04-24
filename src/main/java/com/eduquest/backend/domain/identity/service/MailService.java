package com.eduquest.backend.domain.identity.service;

public interface MailService {

    void sendSignUpMail(String recipientEmail);

    void sendFindIdEmail(String recipientEmail);

    void sendResetPasswordEmail(String recipientEmail);

    boolean isValidToken(String token);

    String findEmailByToken(String token);

    void deleteToken(String token);

}
