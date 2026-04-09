package com.eduquest.backend.infrastructure.persistence.learning.mapper;

import com.eduquest.backend.domain.learning.model.Stage;
import com.eduquest.backend.infrastructure.persistence.learning.entity.StageEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StageEntityMapper {

    public StageEntity toEntity(Stage stage) {
        if (stage == null) return null;
        return StageEntity.of(stage.getTitle(), stage.getNumber(), stage.getReward());
    }

    public Stage toDomain(StageEntity entity) {
        if (entity == null) return null;
        return Stage.of(entity.getTitle(), entity.getNumber(), entity.getReward());
    }

    public List<Stage> toDomainList(List<StageEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

}

