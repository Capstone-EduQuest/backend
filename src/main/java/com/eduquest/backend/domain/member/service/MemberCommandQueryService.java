package com.eduquest.backend.domain.member.service;

import com.eduquest.backend.domain.member.model.Member;

public interface MemberCommandQueryService {

    Long saveMember(Member member, String role);

}
