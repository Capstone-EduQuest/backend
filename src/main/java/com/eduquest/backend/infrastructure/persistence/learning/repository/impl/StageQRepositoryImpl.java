package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

import com.eduquest.backend.domain.learning.dto.StageQuery;
import com.eduquest.backend.domain.progress.dto.ProgressQuery;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QProblemEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QStageEntity;
import com.querydsl.core.types.OrderSpecifier;
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

    public List<StageQuery.Summary> findStageSummariesWithRewardByPagination(int page, int size, String sort, Boolean isAsc) {
        QStageEntity stage = QStageEntity.stageEntity;

        OrderSpecifier<?> order;

        boolean ascending = isAsc != null && isAsc;

        if ("title".equalsIgnoreCase(sort)) {
            order = ascending ? stage.title.asc() : stage.title.desc();
        } else if ("reward".equalsIgnoreCase(sort)) {
            order = ascending ? stage.reward.asc() : stage.reward.desc();
        } else if ("number".equalsIgnoreCase(sort)) {
            order = ascending ? stage.number.asc() : stage.number.desc();
        } else {
            // default
            order = ascending ? stage.number.asc() : stage.number.desc();
        }

        long offset = Math.max(0, (long) page) * Math.max(1, size);

        return queryFactory.select(
                        Projections.constructor(
                                StageQuery.Summary.class,
                                stage.id,
                                stage.uuid,
                                stage.title,
                                stage.number,
                                stage.reward
                        )
                )
                .from(stage)
                .orderBy(order)
                .offset(offset)
                .limit(Math.max(1, size))
                .fetch();
    }

}
