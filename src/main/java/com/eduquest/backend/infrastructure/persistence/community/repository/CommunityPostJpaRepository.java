package com.eduquest.backend.infrastructure.persistence.community.repository;

import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommunityPostJpaRepository extends JpaRepository<CommunityPostEntity, Long> {

    Optional<CommunityPostEntity> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);
}

