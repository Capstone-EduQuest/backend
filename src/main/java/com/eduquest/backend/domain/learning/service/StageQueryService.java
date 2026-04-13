package com.eduquest.backend.domain.learning.service;

import com.eduquest.backend.domain.progress.dto.ProgressQuery;

import java.util.List;
import java.util.UUID;

public interface StageQueryService {

    Long findIdByUuid(UUID uuid);

    Long findRewardById(Long stageId);

    List<ProgressQuery.Detail> findAllStageSummaries();

}

