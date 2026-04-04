package com.eduquest.backend.application.identity.service;

import com.eduquest.backend.application.identity.exception.AuthErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.member.component.CustomPasswordEncoder;
import com.eduquest.backend.domain.member.event.FindIdMailEvent;
import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.service.MailService;
import com.eduquest.backend.domain.member.service.MemberCommandService;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import com.eduquest.backend.presentation.identity.dto.request.FindPasswordRequest;
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

        eventPublisher.publishEvent(
                FindIdMailEvent.of(email)
        );

    }

    public void sendPasswordRestEmail(String email, String userId) {

        eventPublisher.publishEvent(
                FindPasswordRequest.of(
                        email,
                        userId
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

}
