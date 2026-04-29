package com.eduquest.backend.infrastructure.persistence.learning.repository;

import com.eduquest.backend.infrastructure.persistence.learning.entity.HintHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HintHistoryJpaRepository extends JpaRepository<HintHistoryEntity, Long> {
}
