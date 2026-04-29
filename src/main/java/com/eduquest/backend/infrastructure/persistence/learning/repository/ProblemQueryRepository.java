package com.eduquest.backend.infrastructure.persistence.learning.repository;

import com.eduquest.backend.infrastructure.persistence.learning.entity.ProblemEntity;
import com.eduquest.backend.infrastructure.persistence.learning.repository.impl.ProblemQRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemQueryRepository extends ProblemQRepository, JpaRepository<ProblemEntity, Long> {

}


