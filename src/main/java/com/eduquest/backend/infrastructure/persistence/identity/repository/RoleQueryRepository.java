package com.eduquest.backend.infrastructure.persistence.identity.repository;

import com.eduquest.backend.infrastructure.persistence.identity.entity.RoleEntity;
import com.eduquest.backend.infrastructure.persistence.identity.repository.impl.RoleQRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleQueryRepository extends RoleQRepository, JpaRepository<RoleEntity, Long> {
}
