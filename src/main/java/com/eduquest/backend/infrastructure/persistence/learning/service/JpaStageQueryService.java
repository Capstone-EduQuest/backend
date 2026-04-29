package com.eduquest.backend.infrastructure.persistence.learning.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import com.eduquest.backend.domain.progress.dto.ProgressQuery;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.learning.entity.StageEntity;
import com.eduquest.backend.infrastructure.persistence.learning.repository.StageQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaStageQueryService implements StageQueryService {

    private final StageQueryRepository stageQueryRepository;

    @Override
    public Long findIdByUuid(UUID uuid) {
        return stageQueryRepository.findByUuid(uuid)
                .map(StageEntity::getId)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));
    }

    @Override
    public Long findRewardById(Long stageId) {
        return stageQueryRepository.findById(stageId)
                .map(StageEntity::getReward)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));
    }

    @Override
    public List<ProgressQuery.Detail> findAllStageSummaries() {
        return stageQueryRepository.findAllStageSummaries();
    }

}

