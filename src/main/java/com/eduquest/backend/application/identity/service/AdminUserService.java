package com.eduquest.backend.application.identity.service;

import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.service.MemberCommandService;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserService {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    public void lockMember(UUID uuid) {

        Member member = memberQueryService.findMemberByUuid(uuid);

        if (!member.getIsLocked()) {
            member.lock();
        }

    }

}
