package com.eduquest.backend.application.identity.listener;

import com.eduquest.backend.domain.member.event.FindIdMailEvent;
import com.eduquest.backend.domain.member.event.ResetPasswordMailEvent;
import com.eduquest.backend.domain.member.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailEventListener {

    private final MailService mailService;

    @Async("mailEventTaskExecutor")
    @EventListener
    public void handleFindIdMailEvent(FindIdMailEvent event) {

        log.info("Received FindIdMailEvent for email: {}", event.email());
        mailService.sendFindIdEmail(event.email());

    }

    @Async("mailEventTaskExecutor")
    @EventListener
    public void handleResetPasswordMailEvent(ResetPasswordMailEvent event) {

        log.info("Received ResetPasswordMailEvent for email: {}", event.email());
        mailService.sendResetPasswordEmail(event.email());

    }


}
