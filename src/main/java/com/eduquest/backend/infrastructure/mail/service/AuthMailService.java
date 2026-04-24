package com.eduquest.backend.infrastructure.mail.service;

import com.eduquest.backend.domain.identity.service.MailService;
import com.eduquest.backend.infrastructure.mail.repository.EmailTokenRepository;
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
    private final EmailTokenRepository emailTokenRepository;

    @Value("${spring.mail.username}")
    private String senderEmailAddress;
    @Value("${app.backend.base-url}")
    private String backendBaseUrl;

    // 인증 url 메일 전송
    @Override
    @Async("mailEventTaskExecutor")
    public void sendSignUpMail(String recipientEmail) {

        if (emailTokenRepository.existsByEmail(recipientEmail)) {
            // 이미 토큰이 존재하는 경우 예외 처리
            log.warn("A signup token already exists for email: {}", recipientEmail);
            return;
        }

        String token = UUID.randomUUID().toString();

        emailTokenRepository.save(token, recipientEmail);

        String verificationUrl = backendBaseUrl + "/api/v1/auth/sign-up/" + token;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(senderEmailAddress);
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("[EduQuest] 이메일 인증 안내");
        mailMessage.setText("회원가입을 완료하려면 아래 링크를 클릭하세요:\n" + verificationUrl
                + "\n\n토큰은 생성 후 10분간 유효하며, 동일 이메일로는 10분 이내에 재발급되지 않습니다.");

        javaMailSender.send(mailMessage);

    }

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

        if (emailTokenRepository.existsByEmail(recipientEmail)) {
            // 이미 토큰이 존재하는 경우 예외 처리
            log.warn("A password reset token already exists for email: {}", recipientEmail);
            return;
        }

        String token = UUID.randomUUID().toString();

        emailTokenRepository.save(token, recipientEmail);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("[EduQuest] 비밀번호 재설정");
        mailMessage.setText("고객님의 비밀번호 변경 토큰은 " + token + "입니다.\n\n토큰은 생성 후 10분간 유효하며, 동일 이메일로는 10분 이내에 재발급되지 않습니다.");
        mailMessage.setFrom(senderEmailAddress);
        javaMailSender.send(mailMessage);

    }

    @Override
    public boolean isValidToken(String token) {
        return emailTokenRepository.existsByToken(token);
    }

    @Override
    public String findEmailByToken(String token) {
        return emailTokenRepository.findEmailByToken(token);
    }

    @Override
    public void deleteToken(String token) {
        emailTokenRepository.deleteByToken(token);
    }

}
