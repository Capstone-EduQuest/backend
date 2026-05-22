package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

import com.eduquest.backend.domain.learning.dto.StageQuery;
import com.eduquest.backend.domain.progress.dto.ProgressQuery;

import java.util.List;

public interface StageQRepository {

	List<ProgressQuery.Detail> findAllStageSummaries();

	List<StageQuery.Summary> findStageSummariesWithRewardByPagination(int page, int size, String sort, Boolean isAsc);

}
