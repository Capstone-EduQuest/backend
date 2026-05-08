package com.eduquest.backend.domain.learning.service;

import com.eduquest.backend.domain.learning.dto.StageQuery;
import com.eduquest.backend.domain.learning.model.Stage;
import com.eduquest.backend.domain.progress.dto.ProgressQuery;

import java.util.List;
import java.util.UUID;

public interface StageQueryService {

    Long findIdByUuid(UUID uuid);

    Long findRewardById(Long stageId);

    Stage findStageByUuid(UUID uuid);

    List<StageQuery.Summary> findStageSummariesWithRewardByPagination(int page, int size, String sort, Boolean isAsc);

    List<ProgressQuery.Detail> findAllStageSummaries();

}

