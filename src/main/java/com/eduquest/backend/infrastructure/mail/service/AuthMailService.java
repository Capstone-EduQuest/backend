package com.eduquest.backend.infrastructure.mail.service;

import com.eduquest.backend.domain.identity.service.MailService;
import com.eduquest.backend.infrastructure.mail.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthMailService implements MailService {

    private final JavaMailSender javaMailSender;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    @Async("mailEventTaskExecutor")
    public void sendFindIdEmail(String recipientEmail) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("[EduQuest] 아이디 찾기");
        mailMessage.setText("고객님의 아이디는 " + "입니다.");
        mailMessage.setFrom(senderEmailAddress);
        javaMailSender.send(mailMessage);

    }

    @Async("mailEventTaskExecutor")
    public void sendResetPasswordEmail(String recipientEmail) {

        if (passwordResetTokenRepository.existsByEmail(recipientEmail)) {
            // 이미 토큰이 존재하는 경우 예외 처리
            log.warn("A password reset token already exists for email: {}", recipientEmail);
            return;
        }

        String token = UUID.randomUUID().toString();

        passwordResetTokenRepository.save(token, recipientEmail);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("[EduQuest] 비밀번호 재설정");
        mailMessage.setText("고객님의 비밀번호 변경 토큰은 " + token + "입니다.");
        mailMessage.setFrom(senderEmailAddress);
        javaMailSender.send(mailMessage);

    }

    @Override
    public boolean isValidToken(String token) {
        return passwordResetTokenRepository.existsByToken(token);
    }

    @Override
    public String findEmailByToken(String token) {
        return passwordResetTokenRepository.findEmailByToken(token);
    }

    @Override
    public void deleteToken(String token) {
        passwordResetTokenRepository.deleteByToken(token);
    }

}
