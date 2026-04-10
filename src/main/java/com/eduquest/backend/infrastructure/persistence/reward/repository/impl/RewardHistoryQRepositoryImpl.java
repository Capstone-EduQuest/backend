package com.eduquest.backend.infrastructure.persistence.reward.repository.impl;

import com.eduquest.backend.domain.reward.model.RewardHistory;
import com.eduquest.backend.infrastructure.persistence.reward.entity.QRewardHistoryEntity;
import com.eduquest.backend.infrastructure.persistence.reward.entity.RewardHistoryEntity;
import com.eduquest.backend.infrastructure.persistence.reward.mapper.RewardHistoryEntityMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RewardHistoryQRepositoryImpl implements RewardHistoryQRepository {

    private final JPAQueryFactory queryFactory;
    private final RewardHistoryEntityMapper mapper;

    @Override
    public Page<RewardHistory> findByUserId(Long userId, Pageable pageable) {

        QRewardHistoryEntity q = QRewardHistoryEntity.rewardHistoryEntity;

        // total count
        long total = Optional.ofNullable(
                queryFactory.select(q.count())
                        .from(q)
                        .where(q.userId.eq(userId))
                        .fetchOne()
        ).orElse(0L);

        if (total == 0L) {
            return new PageImpl<>(List.of(), pageable, 0L);
        }

        // fetch page content
        List<RewardHistoryEntity> entities = queryFactory.selectFrom(q)
                .where(q.userId.eq(userId))
                .orderBy(q.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<RewardHistory> content = entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);
    }
}
