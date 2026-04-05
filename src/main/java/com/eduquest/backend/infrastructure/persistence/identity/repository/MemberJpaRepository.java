package com.eduquest.backend.infrastructure.persistence.identity.repository;

import com.eduquest.backend.infrastructure.persistence.identity.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByUuid(UUID uuid);

    boolean existsByEmail(String email);

    boolean existsByUserId(String userId);

    void deleteByUuid(UUID uuid);

}
