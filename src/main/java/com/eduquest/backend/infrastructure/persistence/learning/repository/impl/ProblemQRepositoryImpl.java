package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.infrastructure.persistence.learning.entity.HintEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QProblemEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QStageEntity;
import com.eduquest.backend.infrastructure.persistence.learning.repository.HintJpaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProblemQRepositoryImpl implements ProblemQRepository {

    private final JPAQueryFactory queryFactory;
    private final HintJpaRepository hintJpaRepository;

    @Override
    public Optional<ProblemQuery.Detail> findByUuid(UUID uuid) {

        QProblemEntity problemEntity = QProblemEntity.problemEntity;
        QStageEntity stageEntity = QStageEntity.stageEntity;

        Long problemId = queryFactory.select(problemEntity.id).from(problemEntity).where(problemEntity.uuid.eq(uuid)).fetchOne();
        if (problemId == null) return Optional.empty();

        ProblemQuery.Detail problem = queryFactory.select(
                        Projections.constructor(
                                ProblemQuery.Detail.class,
                                problemEntity.id,
                                problemEntity.uuid,
                                stageEntity.uuid,
                                stageEntity.title,
                                stageEntity.number,
                                problemEntity.type,
                                problemEntity.number,
                                problemEntity.summary,
                                problemEntity.example,
                                problemEntity.expectedOutput,
                                problemEntity.block,
                                Expressions.constant(null)
                        )
                )
                .from(problemEntity)
                .join(stageEntity).on(problemEntity.stageId.eq(stageEntity.id))
                .where(problemEntity.id.eq(problemId))
                .fetchOne();

        List<ProblemQuery.Hint> hints = hintJpaRepository.findAllByProblemId(problemId).stream()
                .map(hit -> ProblemQuery.Hint.of(hit.getLevel(), hit.getPoint(), hit.getContent()))
                .collect(Collectors.toList());

        assert problem != null;
        ProblemQuery.Detail result = ProblemQuery.Detail.of(
                problem.id(),
                problem.uuid(),
                problem.stageUuid(),
                problem.stageTitle(),
                problem.stageNumber(),
                problem.type(),
                problem.number(),
                problem.summary(),
                problem.example(),
                problem.expectedOutput(),
                problem.block(),
                hints
        );

        return Optional.of(result);
    }

    @Override
    public List<ProblemQuery.Summary> findAllByStageNumber(Integer stageNumber) {

        QProblemEntity problemEntity = QProblemEntity.problemEntity;
        QStageEntity stageEntity = QStageEntity.stageEntity;

        return queryFactory.select(
                        Projections.constructor(
                                ProblemQuery.Summary.class,
                                problemEntity.uuid,
                                problemEntity.number,
                                problemEntity.summary
                        )
                )
                .from(problemEntity)
                .join(stageEntity).on(problemEntity.stageId.eq(stageEntity.id))
                .where(stageEntity.number.eq(stageNumber))
                .fetch();
    }

    @Override
    public List<ProblemQuery.Detail> findDetailsByStageNumber(Integer stageNumber) {

        QProblemEntity problemEntity = QProblemEntity.problemEntity;
        QStageEntity stageEntity = QStageEntity.stageEntity;

        List<ProblemQuery.Detail> problems = queryFactory.select(
                        Projections.constructor(
                                ProblemQuery.Detail.class,
                                problemEntity.id,
                                problemEntity.uuid,
                                stageEntity.uuid,
                                stageEntity.title,
                                stageEntity.number,
                                problemEntity.type,
                                problemEntity.number,
                                problemEntity.summary,
                                problemEntity.example,
                                problemEntity.expectedOutput,
                                problemEntity.block,
                                Expressions.constant(null)
                        )
                )
                .from(problemEntity)
                .join(stageEntity).on(problemEntity.stageId.eq(stageEntity.id))
                .where(stageEntity.number.eq(stageNumber))
                .fetch();

        if (problems == null || problems.isEmpty()) {
            return List.of();
        };

        List<Long> problemIds = queryFactory.select(problemEntity.id)
                .from(problemEntity)
                .join(stageEntity).on(problemEntity.stageId.eq(stageEntity.id))
                .where(stageEntity.number.eq(stageNumber))
                .fetch();

        List<HintEntity> hintEntities = hintJpaRepository.findAllByProblemIdIn(problemIds);

        Map<Long, List<HintEntity>> hintsByProblem = hintEntities.stream().collect(Collectors.groupingBy(HintEntity::getProblemId));

        return problems.stream().map(pq -> {
            List<ProblemQuery.Hint> hs = hintsByProblem.getOrDefault(pq.id(), List.of()).stream()
                    .map(h -> ProblemQuery.Hint.of(h.getLevel(), h.getPoint(), h.getContent()))
                    .collect(Collectors.toList());

            return ProblemQuery.Detail.of(
                    pq.id(), pq.uuid(), pq.stageUuid(), pq.stageTitle(), pq.stageNumber(), pq.type(), pq.number(), pq.summary(), pq.example(), pq.expectedOutput(), pq.block(), hs
            );
        }).collect(Collectors.toList());
    }

}

