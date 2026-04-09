package com.eduquest.backend.infrastructure.persistence.learning.mapper;

import com.eduquest.backend.domain.learning.model.Hint;
import com.eduquest.backend.infrastructure.persistence.learning.entity.HintEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HintEntityMapper {

    public HintEntity toEntity(Hint hint, Long problemId) {
        if (hint == null) return null;

        return HintEntity.of(problemId, hint.getLevel(), hint.getPoint(), hint.getContent());
    }

    public Hint toDomain(HintEntity entity) {
        if (entity == null) return null;

        return Hint.of(entity.getLevel(), entity.getPoint(), entity.getContent(), entity.getProblemId());
    }

    public List<Hint> toDomainList(List<HintEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

}

