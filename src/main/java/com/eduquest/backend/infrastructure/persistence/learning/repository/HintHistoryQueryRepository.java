package com.eduquest.backend.infrastructure.persistence.learning.repository;

import com.eduquest.backend.infrastructure.persistence.learning.entity.HintHistoryEntity;
import com.eduquest.backend.infrastructure.persistence.learning.repository.impl.HintHistoryQRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HintHistoryQueryRepository extends JpaRepository<HintHistoryEntity, Long>, HintHistoryQRepository {
}
