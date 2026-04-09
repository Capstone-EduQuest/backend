package com.eduquest.backend.domain.learning.service;

import com.eduquest.backend.domain.learning.dto.ProblemQuery;

import java.util.List;
import java.util.UUID;

public interface ProblemQueryService {

    ProblemQuery.Detail findProblemByUuid(UUID uuid);

    List<ProblemQuery.Summary> findAllByStageNumber(Integer stageNumber);

    List<ProblemQuery.Detail> findAllDetailsByStageNumber(Integer stageNumber);

}

