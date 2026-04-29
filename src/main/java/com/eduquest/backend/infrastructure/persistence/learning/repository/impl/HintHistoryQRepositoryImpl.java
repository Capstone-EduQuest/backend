package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

import com.eduquest.backend.infrastructure.persistence.learning.entity.QHintHistoryEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HintHistoryQRepositoryImpl implements HintHistoryQRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isExistsByHintIdAndMemberId(Long hintId, Long memberId) {
        QHintHistoryEntity hintHistory = QHintHistoryEntity.hintHistoryEntity;

        Long count = queryFactory
                .select(hintHistory.count())
                .from(hintHistory)
                .where(hintHistory.hintId.eq(hintId)
                        .and(hintHistory.memberId.eq(memberId)))
                .fetchOne();

        return count != null && count > 0;
    }
}
