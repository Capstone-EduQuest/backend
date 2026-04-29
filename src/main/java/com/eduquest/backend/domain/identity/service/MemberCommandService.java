package com.eduquest.backend.domain.identity.service;

import com.eduquest.backend.domain.identity.model.Member;
import com.eduquest.backend.domain.identity.model.Role;

import java.util.UUID;

public interface MemberCommandService {

    Long saveMember(Member member, String role);

    Long saveRole(Role role);

    Long updateMember(Member member);

    Long updateUserRole(Long id, Long roleId);

    void deleteMember(UUID uuid);

}
