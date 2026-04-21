package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QHintEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QProblemEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HintQRepositoryImpl implements HintQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> findIdByProblemUuidAndLevel(UUID problemUuid, int level) {
        return Optional.ofNullable(
                queryFactory.select(QHintEntity.hintEntity.id)
                        .from(QHintEntity.hintEntity)
                        .join(QProblemEntity.problemEntity)
                        .on(QProblemEntity.problemEntity.id.eq(QHintEntity.hintEntity.problemId))
                        .where(QProblemEntity.problemEntity.uuid.eq(problemUuid)
                                .and(QHintEntity.hintEntity.level.eq(level)))
                        .fetchOne()
        );
    }

    @Override
    public Optional<ProblemQuery.HintDetail> findHintDetailByProblemUuidAndLevel(UUID problemUuid, Integer level) {
        return Optional.ofNullable(
                queryFactory.select(
                                Projections.constructor(
                                        ProblemQuery.HintDetail.class,
                                        QHintEntity.hintEntity.id,
                                        QProblemEntity.problemEntity.id,
                                        QHintEntity.hintEntity.level,
                                        QHintEntity.hintEntity.point,
                                        QHintEntity.hintEntity.content
                ))
                        .from(QHintEntity.hintEntity)
                        .join(QProblemEntity.problemEntity)
                        .on(QProblemEntity.problemEntity.id.eq(QHintEntity.hintEntity.problemId))
                        .where(QProblemEntity.problemEntity.uuid.eq(problemUuid)
                                .and(QHintEntity.hintEntity.level.eq(level)))
                        .fetchOne()
        );
    }
}
