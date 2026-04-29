package com.eduquest.backend.infrastructure.persistence.identity.repository.impl;

import com.eduquest.backend.domain.identity.dto.MemberQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleQRepository {

    List<MemberQuery.Role> findAllRoles();

    Optional<Long> findIdByUuid(UUID uuid);

}
