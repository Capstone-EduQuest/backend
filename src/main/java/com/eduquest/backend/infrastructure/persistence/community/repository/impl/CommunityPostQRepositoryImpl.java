package com.eduquest.backend.infrastructure.persistence.community.repository.impl;

import com.eduquest.backend.domain.community.dto.QuestionQuery;
import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityPostEntity;
import com.eduquest.backend.infrastructure.persistence.community.entity.QCommunityPostEntity;
import com.eduquest.backend.infrastructure.persistence.identity.entity.QMemberEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    public Page<QuestionQuery.Summary> findSummaryBy(Pageable pageable) {
        QCommunityPostEntity post = QCommunityPostEntity.communityPostEntity;
        QMemberEntity member = QMemberEntity.memberEntity;

        List<QuestionQuery.Summary> content = queryFactory
                .select(
                        Projections.constructor(
                                QuestionQuery.Summary.class,
                                post.uuid,
                                post.title,
                                member.uuid,
                                member.nickname,
                                post.createdAt
                        )
                )
                .from(post)
                .leftJoin(member).on(member.id.eq(post.userId))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(post.count()).from(post).fetchOne();
        if (total == null) total = 0L;

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionQuery.Summary> findSummaryBy(Pageable pageable, String searchBy, String keyword, String sortBy, boolean isAsc) {
        QCommunityPostEntity post = QCommunityPostEntity.communityPostEntity;
        QMemberEntity member = QMemberEntity.memberEntity;

        BooleanExpression searchPredicate = buildSearchPredicate(post, member, searchBy, keyword);
        List<OrderSpecifier<?>> orderSpecifiers = buildOrderBy(post, sortBy, isAsc ? "asc" : "desc");

        List<QuestionQuery.Summary> content = queryFactory
                .select(
                        Projections.constructor(
                                QuestionQuery.Summary.class,
                                post.uuid,
                                post.title,
                                member.uuid,
                                member.nickname,
                                post.createdAt
                        )
                )
                .from(post)
                .leftJoin(member).on(member.id.eq(post.userId))
                .where(searchPredicate)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(post.count()).from(post).where(searchPredicate).fetchOne();
        if (total == null) total = 0L;

        return new PageImpl<>(content, pageable, total);
    }

    private List<OrderSpecifier<?>> buildOrderBy(QCommunityPostEntity post, String sortBy, String sortDirection) {
        if (sortDirection == null) sortDirection = "asc";
        if (sortBy == null) sortBy = "created_at";

        if (sortDirection.equals("asc")) {
            return switch (sortBy) {
                case "created_at" -> List.of(post.createdAt.asc());
                default -> List.of(post.id.asc());
            };
        } else if (sortDirection.equals("desc")) {
            return switch (sortBy) {
                case "created_at" -> List.of(post.createdAt.desc());
                default -> List.of(post.id.desc());
            };
        } else {
            throw new IllegalArgumentException("Invalid sort direction: " + sortDirection);
        }
    }

    private BooleanExpression buildSearchPredicate(QCommunityPostEntity post, QMemberEntity member, String searchBy, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return switch (searchBy) {
            case "title" -> post.title.containsIgnoreCase(keyword);
            case "content" -> post.content.containsIgnoreCase(keyword);
            case "author" -> member.nickname.containsIgnoreCase(keyword);
            case null, default -> post.title.containsIgnoreCase(keyword)
                    .or(post.content.containsIgnoreCase(keyword));
        };
    }

    @Override
    public Optional<CommunityPostEntity> findByUuid(UUID uuid) {
        QCommunityPostEntity post = QCommunityPostEntity.communityPostEntity;
        CommunityPostEntity entity = queryFactory.selectFrom(post).where(post.uuid.eq(uuid)).fetchOne();
        return Optional.ofNullable(entity);
    }
}

