package com.eduquest.backend.infrastructure.persistence.learning.mapper;

import com.eduquest.backend.domain.learning.model.Hint;
import com.eduquest.backend.domain.learning.model.Problem;
import com.eduquest.backend.infrastructure.persistence.learning.entity.HintEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.ProblemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProblemEntityMapper {

    public ProblemEntity toEntity(Problem problem) {
        if (problem == null) return null;

        return ProblemEntity.of(
                problem.getStageId(),
                problem.getType(),
                problem.getNumber(),
                problem.getSummary(),
                problem.getExample(),
                problem.getExpectedOutput(),
                problem.getBlock()
        );
    }

    public ProblemEntity toEntityWithIdAndUuid(Problem problem) {
        if (problem == null) return null;
        ProblemEntity entity = toEntity(problem);
        if (problem.getUuid() != null) {
            entity.setUuid(problem.getUuid());
        }
        if (problem.getId() != null) {
            entity.setId(problem.getId());
        }
        return entity;
    }

    public Problem toDomain(ProblemEntity entity, List<HintEntity> hintEntities) {
        if (entity == null) return null;

        List<Hint> hints = hintEntities == null ? List.of() : hintEntities.stream()
                .map(h -> Hint.of(h.getLevel(), h.getPoint(), h.getContent(), h.getProblemId()))
                .collect(Collectors.toList());

        return Problem.of(entity.getStageId(), entity.getType(), entity.getNumber(), entity.getSummary(), entity.getExample(), entity.getExpectedOutput(), entity.getBlock(), hints);
    }

}

