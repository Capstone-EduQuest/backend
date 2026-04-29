package com.eduquest.backend.infrastructure.persistence.learning.repository;

import com.eduquest.backend.infrastructure.persistence.learning.entity.StageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StageJpaRepository extends JpaRepository<StageEntity, Long> {

    Optional<StageEntity> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

}

