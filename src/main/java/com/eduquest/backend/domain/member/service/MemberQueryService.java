package com.eduquest.backend.domain.member.service;

import com.eduquest.backend.domain.member.dto.MemberQuery;
import com.eduquest.backend.domain.member.model.Member;

import java.util.List;
import java.util.UUID;

public interface MemberQueryService {

    boolean isExistById(Long id);

    boolean isExistByEmail(String email);

    Member findMemberById(Long id);

    Member findMemberByUuid(UUID uuid);

    Member findMemberByEmail(String email);

    MemberQuery.EmailAndUserId findEmailAndUserIdByEmail(String email);

    UUID findMemberUuidByUserId(String userId);

    Long findMemberIdByUuid(UUID uuid);

    Long findMemberIdByUserId(String userId);

    Long findRoleIdByUuid(UUID uuid);

    MemberQuery.UserProfile findUserProfileByUuid(UUID uuid);

    List<MemberQuery.UserListResult> findAllMembersByPagination(int page, int size, String sortBy, String sortDirection);

    List<MemberQuery.Role> findAllRoles();

}
