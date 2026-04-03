package com.eduquest.backend.infrastructure.persistence.identity.repository.impl;

import com.eduquest.backend.domain.member.dto.MemberQuery;
import com.eduquest.backend.domain.member.dto.UserDetailsData;

import java.util.Optional;

public interface MemberQRepository {

    boolean existsByEmail(String email);

    Optional<UserDetailsData> findUserDetailsByUserId(String userId);

    Optional<MemberQuery.EmailAndUserId> findEmailAndUserIdByEmail(String email);

}
