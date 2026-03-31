package com.eduquest.backend.application.identity.service;

import com.eduquest.backend.application.identity.dto.ProfileCommand;
import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.service.MemberCommandQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final MemberCommandQueryService memberCommandQueryService;

    // 회원 가입 처리
    public void signUp(ProfileCommand command) {

        Member member = Member.of(
                command.id(),
                command.email(),
                command.password(),
                command.birth(),
                command.nickname(),
                false,
                null
        );

        memberCommandQueryService.saveMember(member, "USER");

    }

}
