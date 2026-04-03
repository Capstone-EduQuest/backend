package com.eduquest.backend.domain.member.service;

public interface MailService {

    void sendFindIdEmail(String recipientEmail);

    void sendResetPasswordEmail(String recipientEmail);

}
