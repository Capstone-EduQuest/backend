package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

import com.eduquest.backend.domain.learning.dto.ProblemQuery;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ProblemQRepository {

	Optional<ProblemQuery.Detail> findByUuid(UUID uuid);

	List<ProblemQuery.Summary> findAllByStageNumber(Integer stageNumber);

	List<ProblemQuery.Detail> findDetailsByStageNumber(Integer stageNumber);

	Map<Integer, List<ProblemQuery.Detail>> findDetailsByStageNumbers(List<Integer> stageNumbers);

	List<ProblemQuery.Detail> findDetailsByPagination(int page, int size, String sort, Boolean isAsc);

}


