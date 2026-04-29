package com.eduquest.backend.infrastructure.persistence.learning.repository;

import com.eduquest.backend.infrastructure.persistence.learning.entity.HintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HintJpaRepository extends JpaRepository<HintEntity, Long> {

    List<HintEntity> findAllByProblemId(Long problemId);

    List<HintEntity> findAllByProblemIdIn(List<Long> problemIds);

}

