package com.eduquest.backend.infrastructure.persistence.identity.repository.impl;

import com.eduquest.backend.domain.member.dto.UserDetailsData;

import java.util.Optional;

public interface MemberQRepository {

    Optional<UserDetailsData> findUserDetailsByUserId(String userId);

}
