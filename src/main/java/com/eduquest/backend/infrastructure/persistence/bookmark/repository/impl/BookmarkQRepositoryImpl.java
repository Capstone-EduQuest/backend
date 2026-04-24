package com.eduquest.backend.infrastructure.persistence.bookmark.repository.impl;

import com.eduquest.backend.domain.bookmark.dto.BookmarkQuery;
import com.eduquest.backend.infrastructure.persistence.bookmark.entity.QBookmarkEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QProblemEntity;
import com.eduquest.backend.infrastructure.persistence.learning.entity.QStageEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookmarkQRepositoryImpl implements BookmarkQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> findProblemIdByUuid(java.util.UUID problemUuid) {
        Long id = queryFactory.select(QProblemEntity.problemEntity.id)
                .from(QProblemEntity.problemEntity)
                .where(QProblemEntity.problemEntity.uuid.eq(problemUuid))
                .fetchOne();
        return Optional.ofNullable(id);
    }

    @Override
    public List<BookmarkQuery.Summary> findByUserId(Long userId, int page, int size, String sortBy, boolean isAsc) {
        QBookmarkEntity bookmark = QBookmarkEntity.bookmarkEntity;
        QProblemEntity problem = QProblemEntity.problemEntity;
        QStageEntity stage = QStageEntity.stageEntity;

        return queryFactory.select(
                        Projections.constructor(
                                BookmarkQuery.Summary.class,
                                stage.title,
                                problem.type,
                                problem.number,
                                problem.uuid
                        )
                )
                .from(bookmark)
                .join(problem).on(bookmark.problemId.eq(problem.id))
                .join(stage).on(problem.stageId.eq(stage.id))
                .where(bookmark.userId.eq(userId))
                .orderBy(buildOrderBy(bookmark, problem, sortBy, isAsc).toArray(new OrderSpecifier[0]))
                .offset((long) Math.max(0, page) * Math.max(1, size))
                .limit(Math.max(1, size))
                .fetch();
    }

    @Override
    public List<BookmarkQuery.Summary> findByPagination(int page, int size, String sortBy, boolean isAsc) {
        QBookmarkEntity bookmark = QBookmarkEntity.bookmarkEntity;
        QProblemEntity problem = QProblemEntity.problemEntity;
        QStageEntity stage = QStageEntity.stageEntity;

        return queryFactory.select(
                        Projections.constructor(
                                BookmarkQuery.Summary.class,
                                stage.title,
                                problem.type,
                                problem.number,
                                problem.uuid
                        )
                )
                .from(bookmark)
                .join(problem).on(bookmark.problemId.eq(problem.id))
                .join(stage).on(problem.stageId.eq(stage.id))
                .orderBy(buildOrderBy(bookmark, problem, sortBy, isAsc).toArray(new OrderSpecifier[0]))
                .offset((long) Math.max(0, page) * Math.max(1, size))
                .limit(Math.max(1, size))
                .fetch();
    }

    @Override
    public Long countByUserId(Long userId) {
        QBookmarkEntity bookmark = QBookmarkEntity.bookmarkEntity;
        return queryFactory.select(bookmark.count())
                .from(bookmark)
                .where(bookmark.userId.eq(userId))
                .fetchOne();
    }

    private java.util.List<OrderSpecifier<?>> buildOrderBy(QBookmarkEntity bookmark, QProblemEntity problem, String sortBy, boolean isAsc) {
        String dir = isAsc ? "asc" : "desc";

        if (!"desc".equalsIgnoreCase(dir) && !"asc".equalsIgnoreCase(dir)) {
            dir = "desc";
        }

        if (dir.equals("asc")) {
            return switch (sortBy == null ? "created_at" : sortBy) {
                case "problem_number" -> java.util.List.of(problem.number.asc());
                case "created_at" -> java.util.List.of(bookmark.createdAt.asc());
                default -> java.util.List.of(bookmark.createdAt.asc());
            };
        } else {
            return switch (sortBy == null ? "created_at" : sortBy) {
                case "problem_number" -> java.util.List.of(problem.number.desc());
                case "created_at" -> java.util.List.of(bookmark.createdAt.desc());
                default -> java.util.List.of(bookmark.createdAt.desc());
            };
        }
    }

}

