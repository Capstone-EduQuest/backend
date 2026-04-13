package com.eduquest.backend.infrastructure.persistence.learning.repository;

import com.eduquest.backend.infrastructure.persistence.learning.entity.StageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StageJpaRepository extends JpaRepository<StageEntity, Long> {

    void deleteByUuid(UUID uuid);

}

