package com.eduquest.backend.infrastructure.persistence.community.repository.impl;

import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityPostEntity;
import com.eduquest.backend.infrastructure.persistence.community.entity.QCommunityPostEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CommunityPostQRepositoryImpl implements CommunityPostQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommunityPostEntity> findAllBy(Pageable pageable) {
        QCommunityPostEntity post = QCommunityPostEntity.communityPostEntity;

        List<CommunityPostEntity> content = queryFactory
                .selectFrom(post)
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(post.count()).from(post).fetchOne();
        if (total == null) total = 0L;

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<CommunityPostEntity> findByUuid(UUID uuid) {
        QCommunityPostEntity post = QCommunityPostEntity.communityPostEntity;
        CommunityPostEntity entity = queryFactory.selectFrom(post).where(post.uuid.eq(uuid)).fetchOne();
        return Optional.ofNullable(entity);
    }
}

