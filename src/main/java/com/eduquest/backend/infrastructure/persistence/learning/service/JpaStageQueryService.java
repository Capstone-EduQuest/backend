package com.eduquest.backend.infrastructure.persistence.learning.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.learning.entity.StageEntity;
import com.eduquest.backend.infrastructure.persistence.learning.repository.StageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaStageQueryService implements StageQueryService {

    private final StageJpaRepository stageJpaRepository;

    @Override
    public Long findIdByUuid(UUID uuid) {
        return stageJpaRepository.findByUuid(uuid)
                .map(StageEntity::getId)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));
    }

    @Override
    public Long findRewardById(Long stageId) {
        return stageJpaRepository.findById(stageId)
                .map(StageEntity::getReward)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));
    }

}

