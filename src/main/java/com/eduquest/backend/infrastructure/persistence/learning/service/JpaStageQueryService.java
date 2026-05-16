package com.eduquest.backend.infrastructure.persistence.learning.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.learning.dto.StageQuery;
import com.eduquest.backend.domain.learning.model.Stage;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import com.eduquest.backend.domain.progress.dto.ProgressQuery;
import com.eduquest.backend.infrastructure.persistence.learning.entity.StageEntity;
import com.eduquest.backend.infrastructure.persistence.learning.exception.LearningDatabaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.learning.mapper.StageEntityMapper;
import com.eduquest.backend.infrastructure.persistence.learning.repository.StageQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaStageQueryService implements StageQueryService {

    private final StageQueryRepository stageQueryRepository;
    private final StageEntityMapper stageEntityMapper;

    @Override
    public Long findIdByUuid(UUID uuid) {
        return stageQueryRepository.findByUuid(uuid)
                .map(StageEntity::getId)
                .orElseThrow(() -> new EduQuestException(LearningDatabaseErrorCode.STAGE_NOT_FOUND));
    }

    @Override
    public Long findRewardById(Long stageId) {
        return stageQueryRepository.findById(stageId)
                .map(StageEntity::getReward)
                .orElseThrow(() -> new EduQuestException(LearningDatabaseErrorCode.STAGE_NOT_FOUND));
    }

    @Override
    public Stage findStageByUuid(UUID uuid) {
        return stageQueryRepository.findByUuid(uuid)
                .map(stageEntityMapper::toDomain)
                .orElseThrow(() -> new EduQuestException(LearningDatabaseErrorCode.STAGE_NOT_FOUND));
    }

    @Override
    public List<ProgressQuery.Detail> findAllStageSummaries() {
        return stageQueryRepository.findAllStageSummaries();
    }

    @Override
    public List<StageQuery.Summary> findStageSummariesWithRewardByPagination(int page, int size, String sort, Boolean isAsc) {
        return stageQueryRepository.findStageSummariesWithRewardByPagination(page, size, sort, isAsc);
    }

}

