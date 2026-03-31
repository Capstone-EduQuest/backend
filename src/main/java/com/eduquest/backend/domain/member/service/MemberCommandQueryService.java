package com.eduquest.backend.domain.member.service;

import com.eduquest.backend.domain.member.model.Member;

public interface MemberCommandQueryService {

    void saveMember(Member member, String role);

}
