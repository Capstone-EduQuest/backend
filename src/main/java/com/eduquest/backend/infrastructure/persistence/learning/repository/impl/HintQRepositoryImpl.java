package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

import com.eduquest.backend.infrastructure.persistence.learning.entity.QHintEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QProblemEntity;
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
}
