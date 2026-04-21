package com.eduquest.backend.infrastructure.persistence.learning.repository;

import com.eduquest.backend.infrastructure.persistence.learning.entity.HintEntity;
import com.eduquest.backend.infrastructure.persistence.learning.repository.impl.HintQRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HintQueryRepository extends JpaRepository<HintEntity, Long>, HintQRepository {
}
