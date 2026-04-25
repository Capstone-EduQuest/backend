package com.eduquest.backend.infrastructure.persistence.identity.repository.impl;

import com.eduquest.backend.domain.identity.dto.MemberQuery;
import com.eduquest.backend.domain.identity.dto.UserDetailsData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberQRepository {

    boolean existsByEmail(String email);

    Optional<UserDetailsData> findUserDetailsByUserId(String userId);

    Optional<MemberQuery.EmailAndUserId> findEmailAndUserIdByEmail(String email);

    Optional<UUID> findUuidByUserId(String userId);

    Optional<Long> findIdByUuid(UUID uuid);

    Optional<Long> findIdByUserId(String userId);

    Optional<MemberQuery.UserProfile> findUserProfileByUuid(UUID uuid);

    List<MemberQuery.UserListResult> findAllMembersByPagination(int page, int size, String sortBy, String sortDirection);

}
