package com.eduquest.backend.infrastructure.persistence.identity.repository;

import com.eduquest.backend.infrastructure.persistence.identity.entity.MemberEntity;
import com.eduquest.backend.infrastructure.persistence.identity.repository.impl.MemberQRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberQueryRepository extends MemberQRepository, JpaRepository<MemberEntity, Long> {

    boolean existsByUserId(String userId);

    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByUuid(UUID uuid);

    Optional<MemberEntity> findByUserId(String userId);

}
