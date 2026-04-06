package com.eduquest.backend.application.identity.service;

import com.eduquest.backend.application.identity.exception.AuthErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.member.component.CustomPasswordEncoder;
import com.eduquest.backend.domain.member.event.FindIdMailEvent;
import com.eduquest.backend.domain.member.event.ResetPasswordMailEvent;
import com.eduquest.backend.domain.member.event.RotateTokenEvent;
import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.service.MailService;
import com.eduquest.backend.domain.member.service.MemberCommandService;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final ApplicationEventPublisher eventPublisher;
    private final MailService mailService;
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final CustomPasswordEncoder passwordEncoder;

    public void sendFindIdEmail(String email) {

        if (!memberQueryService.isExistByEmail(email)) {
            return;
        }

        eventPublisher.publishEvent(
                FindIdMailEvent.of(email)
        );

    }

    public void sendPasswordRestEmail(String email, String userId) {

        if (!memberQueryService.isExistByEmail(email)) {
            return;
        }

        eventPublisher.publishEvent(
                ResetPasswordMailEvent.of(
                        userId,
                        email
                )
        );

    }

    public void resetPassword(String token, String newPassword) {

        if (!mailService.isValidToken(token)) {
            throw new EduQuestException(AuthErrorCode.INVALID_PASSWORD_RESET_TOKEN);
        }

        Member member = memberQueryService.findMemberByEmail(mailService.findEmailByToken(token));

        member.updatePassword(
                passwordEncoder.encode(newPassword)
        );

        memberCommandService.updateMember(member);

        mailService.deleteToken(token);

    }

    public void rotateRefreshToken(String refreshToken, HttpServletResponse response) {

        eventPublisher.publishEvent(
                RotateTokenEvent.of(refreshToken, response)
        );

    }

}
