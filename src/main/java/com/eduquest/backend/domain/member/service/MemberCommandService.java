package com.eduquest.backend.domain.member.service;

import com.eduquest.backend.domain.member.model.Member;

public interface MemberCommandService {

    Long saveMember(Member member, String role);

    Long updateMember(Member member);

}
