package com.eduquest.backend.application.identity.service;

import com.eduquest.backend.domain.identity.event.AccountLockEvent;
import com.eduquest.backend.domain.identity.model.Member;
import com.eduquest.backend.domain.identity.service.MemberCommandService;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserService {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final ApplicationEventPublisher eventPublisher;

    public void lockMember(UUID uuid) {

        Member member = memberQueryService.findMemberByUuid(uuid);

        if (!member.getIsLocked()) {
            member.lock();
        }

        memberCommandService.updateMember(member);

        eventPublisher.publishEvent(
                AccountLockEvent.of(member.getUserId())
        );

    }

}
