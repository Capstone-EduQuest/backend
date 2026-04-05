package com.eduquest.backend.domain.member.service;

import com.eduquest.backend.domain.member.model.Member;

import java.util.UUID;

public interface MemberCommandService {

    Long saveMember(Member member, String role);

    Long updateMember(Member member);

    Long updateUserRole(Long id, Long roleId);

    void deleteMember(UUID uuid);

}
