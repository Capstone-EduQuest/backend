package com.eduquest.backend.domain.member.service;

import com.eduquest.backend.domain.member.dto.MemberQuery;
import com.eduquest.backend.domain.member.model.Member;

import java.util.List;
import java.util.UUID;

public interface MemberQueryService {

    boolean isExistByEmail(String email);

    Member findMemberByEmail(String email);

    MemberQuery.EmailAndUserId findEmailAndUserIdByEmail(String email);

    UUID findUuidByUserId(String userId);

    MemberQuery.UserProfile findUserProfileByUuid(UUID uuid);

    List<MemberQuery.UserListResult> findAllMembersByPagination(int page, int size, String sortBy, String sortDirection);

}
