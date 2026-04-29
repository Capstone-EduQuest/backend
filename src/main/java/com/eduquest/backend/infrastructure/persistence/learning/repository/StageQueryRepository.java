package com.eduquest.backend.infrastructure.persistence.learning.repository;

import com.eduquest.backend.infrastructure.persistence.learning.entity.StageEntity;
import com.eduquest.backend.infrastructure.persistence.learning.repository.impl.StageQRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StageQueryRepository extends JpaRepository<StageEntity, Long>, StageQRepository {

    Optional<StageEntity> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

}
