package com.eduquest.backend.domain.learning.service;

import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.domain.learning.model.Problem;

import java.util.List;
import java.util.UUID;

public interface ProblemQueryService {

    Problem findProblemById(Long id);

    ProblemQuery.Detail findProblemByUuid(UUID uuid);

    ProblemQuery.HintDetail findHintByProblemUuidAndLevel(UUID uuid, Integer level);

    Long findHintIdByProblemUuidAndLevel(UUID problemUuid, int level);

    List<ProblemQuery.Detail> findAllDetailsByStageNumber(Integer stageNumber);

    List<ProblemQuery.Detail> findDetailsByPagination(int page, int size, String sort, Boolean isAsc);

}

