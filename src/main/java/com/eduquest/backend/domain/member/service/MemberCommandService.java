package com.eduquest.backend.domain.member.service;

import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.model.Role;
import com.eduquest.backend.domain.member.model.enums.RoleType;

import java.util.UUID;

public interface MemberCommandService {

    Long saveMember(Member member, String role);

    Long saveRole(Role role);

    Long updateMember(Member member);

    Long updateUserRole(Long id, Long roleId);

    void deleteMember(UUID uuid);

}
