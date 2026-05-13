package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.infrastructure.persistence.learning.entity.HintEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.ProblemEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QProblemEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QStageEntity;
import com.eduquest.backend.infrastructure.persistence.learning.repository.HintJpaRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
                                Expressions.constant(List.of())
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
                                problemEntity.block
                        )
                )
                .from(problemEntity)
                .join(stageEntity).on(problemEntity.stageId.eq(stageEntity.id))
                .where(stageEntity.number.eq(stageNumber))
                .orderBy(problemEntity.number.asc())
                .fetch();

        if (problems.isEmpty()) {
            return List.of();
        }

        List<Long> problemIds = problems.stream()
                .map(ProblemQuery.Detail::id)
                .collect(Collectors.toList());

        List<HintEntity> hintEntities = hintJpaRepository.findAllByProblemIdIn(problemIds);
        Map<Long, List<HintEntity>> hintsByProblem = hintEntities.stream()
                .collect(Collectors.groupingBy(HintEntity::getProblemId));

        return problems.stream().map(pq -> {
            List<ProblemQuery.Hint> hs = hintsByProblem.getOrDefault(pq.id(), List.of()).stream()
                    .map(hint -> ProblemQuery.Hint.of(hint.getLevel(), hint.getPoint(), hint.getContent()))
                    .collect(Collectors.toList());
            return ProblemQuery.Detail.of(
                    pq.id(), pq.uuid(), pq.stageUuid(), pq.stageTitle(), pq.stageNumber(),
                    pq.type(), pq.number(), pq.summary(), pq.example(), pq.expectedOutput(), pq.block(), hs
            );
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Integer, List<ProblemQuery.Detail>> findDetailsByStageNumbers(List<Integer> stageNumbers) {

        if (stageNumbers == null || stageNumbers.isEmpty()) {
            return Map.of();
        }

        QProblemEntity problemEntity = QProblemEntity.problemEntity;
        QStageEntity stageEntity = QStageEntity.stageEntity;

        List<ProblemQuery.Detail> problemDetailList = queryFactory.select(
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
                                Expressions.constant(List.of())
                        )
                )
                .from(problemEntity)
                .join(stageEntity).on(problemEntity.stageId.eq(stageEntity.id))
                .where(stageEntity.number.in(stageNumbers))
                .fetch();

        if (problemDetailList == null || problemDetailList.isEmpty()) {
            return Map.of();
        }

        List<Long> problemIdList = problemDetailList.stream()
                .map(ProblemQuery.Detail::id)
                .toList();

        List<HintEntity> hintEntityList = hintJpaRepository.findAllByProblemIdIn(problemIdList);

        Map<Long, List<HintEntity>> hintsByProblemId = hintEntityList.stream()
                .collect(Collectors.groupingBy(HintEntity::getProblemId));

        List<ProblemQuery.Detail> enrichedProblemDetailList = problemDetailList.stream()
                .map(problemDetail -> {
                    List<HintEntity> hintEntitiesForProblem = hintsByProblemId.getOrDefault(problemDetail.id(), List.of());
                    List<ProblemQuery.Hint> hintList = hintEntitiesForProblem.stream()
                            .map(hintEntity -> ProblemQuery.Hint.of(hintEntity.getLevel(), hintEntity.getPoint(), hintEntity.getContent()))
                            .collect(Collectors.toList());

                    return ProblemQuery.Detail.of(
                            problemDetail.id(),
                            problemDetail.uuid(),
                            problemDetail.stageUuid(),
                            problemDetail.stageTitle(),
                            problemDetail.stageNumber(),
                            problemDetail.type(),
                            problemDetail.number(),
                            problemDetail.summary(),
                            problemDetail.example(),
                            problemDetail.expectedOutput(),
                            problemDetail.block(),
                            hintList
                    );
                })
                .toList();

        return enrichedProblemDetailList.stream()
                .collect(Collectors.groupingBy(
                        ProblemQuery.Detail::stageNumber,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

    }

    @Override
    public List<ProblemQuery.Detail> findDetailsByPagination(int page, int size, String sort, Boolean isAsc) {

        QProblemEntity problemEntity = QProblemEntity.problemEntity;
        QStageEntity stageEntity = QStageEntity.stageEntity;

        OrderSpecifier<?> order;

        boolean ascending = isAsc != null && isAsc;

        if ("number".equalsIgnoreCase(sort)) {
            order = ascending ? problemEntity.number.asc() : problemEntity.number.desc();
        } else if ("type".equalsIgnoreCase(sort)) {
            order = ascending ? problemEntity.type.asc() : problemEntity.type.desc();
        } else if ("created_at".equalsIgnoreCase(sort) || "createdAt".equalsIgnoreCase(sort)) {
            order = ascending ? problemEntity.createdAt.asc() : problemEntity.createdAt.desc();
        } else {
            // default sort by createdAt desc
            order = ascending ? problemEntity.createdAt.asc() : problemEntity.createdAt.desc();
        }

        long offset = Math.max(0, (long) page) * Math.max(1, size);

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
                                Expressions.constant(List.of())
                        )
                )
                .from(problemEntity)
                .join(stageEntity).on(problemEntity.stageId.eq(stageEntity.id))
                .orderBy(order)
                .offset(offset)
                .limit(Math.max(1, size))
                .fetch();

        if (problems == null || problems.isEmpty()) {
            return List.of();
        }

        List<Long> problemIds = problems.stream().map(ProblemQuery.Detail::id).collect(Collectors.toList());

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

