package com.eduquest.backend.infrastructure.persistence.learning.service;

import com.eduquest.backend.domain.learning.model.Stage;
import com.eduquest.backend.domain.learning.service.StageCommandService;
import com.eduquest.backend.infrastructure.persistence.learning.entity.StageEntity;
import com.eduquest.backend.infrastructure.persistence.learning.mapper.StageEntityMapper;
import com.eduquest.backend.infrastructure.persistence.learning.repository.StageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaStageCommandService implements StageCommandService {

    private final StageJpaRepository stageJpaRepository;
    private final StageEntityMapper stageEntityMapper;

    @Override
    public Long saveStage(Stage stage) {

        StageEntity entity = stageEntityMapper.toEntity(stage);
        entity = stageJpaRepository.save(entity);

        return entity.getId();
    }

    @Transactional
    @Override
    public Long updateStage(Stage stage) {

        StageEntity entity = stageEntityMapper.toEntity(stage);
        entity = stageJpaRepository.save(entity);

        return entity.getId();
    }

    @Transactional
    @Override
    public void deleteStage(UUID uuid) {
        stageJpaRepository.findByUuid(uuid).ifPresent(stageJpaRepository::delete);
    }

}

