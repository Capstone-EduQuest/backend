package com.eduquest.backend.application.identity.service;

import com.eduquest.backend.application.identity.dto.RoleCommand;
import com.eduquest.backend.domain.member.service.MemberCommandService;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    public List<RoleCommand.RoleResponse> getRoles() {
        return memberQueryService.findAllRoles().stream()
                .map(role -> RoleCommand.RoleResponse.of(role.uuid(), role.name()))
                .toList();
    }

    public void updateRole(RoleCommand.RoleUpdateRequest request) {

        Long memberId = memberQueryService.findMemberIdByUuid(request.userUuid());
        Long roleId = memberQueryService.findRoleIdByUuid(request.roleUuid());

        memberCommandService.updateUserRole(memberId, roleId);

    }

}
