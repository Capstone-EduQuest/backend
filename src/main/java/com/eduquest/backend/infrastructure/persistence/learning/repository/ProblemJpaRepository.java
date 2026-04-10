package com.eduquest.backend.infrastructure.persistence.learning.repository;

import com.eduquest.backend.infrastructure.persistence.learning.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProblemJpaRepository extends JpaRepository<ProblemEntity, Long> {

    Optional<ProblemEntity> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    List<ProblemEntity> findAllByStageId(Long stageId);

}

