package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

import com.eduquest.backend.domain.progress.dto.ProgressQuery;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QProblemEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QStageEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class StageQRepositoryImpl implements StageQRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProgressQuery.Detail> findAllStageSummaries() {
        QStageEntity stage = QStageEntity.stageEntity;
        QProblemEntity problem = QProblemEntity.problemEntity;

        return queryFactory.select(
                        Projections.constructor(
                                ProgressQuery.Detail.class,
                                stage.id,
                                stage.uuid,
                                stage.title,
                                stage.number,
                                problem.id.count()
                        )
                )
                .from(stage)
                .leftJoin(problem).on(problem.stageId.eq(stage.id))
                .groupBy(stage.id, stage.uuid, stage.title, stage.number)
                .orderBy(stage.number.asc())
                .fetch();
    }

}
