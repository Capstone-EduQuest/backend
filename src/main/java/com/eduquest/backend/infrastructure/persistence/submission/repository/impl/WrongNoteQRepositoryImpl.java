package com.eduquest.backend.infrastructure.persistence.submission.repository.impl;

import com.eduquest.backend.domain.submission.dto.WrongNoteQuery;
import com.eduquest.backend.infrastructure.persistence.submission.entity.QWrongNoteEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WrongNoteQRepositoryImpl implements WrongNoteQRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<WrongNoteQuery.Detail> findByUserIdAndProblemId(Long userId, Long problemId) {

        return Optional.ofNullable(
                queryFactory.select(
                        Projections.constructor(
                                WrongNoteQuery.Detail.class,
                                QWrongNoteEntity.wrongNoteEntity.uuid,
                                QWrongNoteEntity.wrongNoteEntity.id,
                                QWrongNoteEntity.wrongNoteEntity.userId,
                                QWrongNoteEntity.wrongNoteEntity.problemId,
                                QWrongNoteEntity.wrongNoteEntity.wrongAnswer,
                                QWrongNoteEntity.wrongNoteEntity.aiExplanation,
                                QWrongNoteEntity.wrongNoteEntity.isReviewed,
                                QWrongNoteEntity.wrongNoteEntity.nextReviewAt,
                                QWrongNoteEntity.wrongNoteEntity.createdAt,
                                QWrongNoteEntity.wrongNoteEntity.updatedAt
                        )
                )
                        .from(QWrongNoteEntity.wrongNoteEntity)
                        .where(QWrongNoteEntity.wrongNoteEntity.userId.eq(userId)
                                .and(QWrongNoteEntity.wrongNoteEntity.problemId.eq(problemId)))
                        .fetchOne()
        );

    }

    @Override
    public List<WrongNoteQuery.Detail> findByUserId(Long userId, int page, int size, String sortBy, boolean isAsc) {
        return queryFactory.select(
                Projections.constructor(
                        WrongNoteQuery.Detail.class,
                        QWrongNoteEntity.wrongNoteEntity.uuid,
                        QWrongNoteEntity.wrongNoteEntity.id,
                        QWrongNoteEntity.wrongNoteEntity.userId,
                        QWrongNoteEntity.wrongNoteEntity.problemId,
                        QWrongNoteEntity.wrongNoteEntity.wrongAnswer,
                        QWrongNoteEntity.wrongNoteEntity.aiExplanation,
                        QWrongNoteEntity.wrongNoteEntity.isReviewed,
                        QWrongNoteEntity.wrongNoteEntity.nextReviewAt,
                        QWrongNoteEntity.wrongNoteEntity.createdAt,
                        QWrongNoteEntity.wrongNoteEntity.updatedAt
                )

        )
                .from(QWrongNoteEntity.wrongNoteEntity)
                .orderBy(buildOrderBy(sortBy, isAsc ? "asc" : "desc").toArray(new OrderSpecifier[0]))
                .offset((long) page * size)
                .limit(size)
                .where(QWrongNoteEntity.wrongNoteEntity.userId.eq(userId))
                .fetch();
    }

    @Override
    public Optional<WrongNoteQuery.Detail> findByUuid(UUID wrongNoteUuid) {
        return Optional.ofNullable(
                queryFactory.select(
                        Projections.constructor(
                                WrongNoteQuery.Detail.class,
                                QWrongNoteEntity.wrongNoteEntity.uuid,
                                QWrongNoteEntity.wrongNoteEntity.id,
                                QWrongNoteEntity.wrongNoteEntity.userId,
                                QWrongNoteEntity.wrongNoteEntity.problemId,
                                QWrongNoteEntity.wrongNoteEntity.wrongAnswer,
                                QWrongNoteEntity.wrongNoteEntity.aiExplanation,
                                QWrongNoteEntity.wrongNoteEntity.isReviewed,
                                QWrongNoteEntity.wrongNoteEntity.nextReviewAt,
                                QWrongNoteEntity.wrongNoteEntity.createdAt,
                                QWrongNoteEntity.wrongNoteEntity.updatedAt
                        )
                )
                        .from(QWrongNoteEntity.wrongNoteEntity)
                        .where(QWrongNoteEntity.wrongNoteEntity.uuid.eq(wrongNoteUuid))
                        .fetchOne()
        );
    }

    @Override
    public List<WrongNoteQuery.Detail> findByPagination(int page, int size, String sortBy, boolean isAsc) {
        return queryFactory.select(
                Projections.constructor(
                        WrongNoteQuery.Detail.class,
                        QWrongNoteEntity.wrongNoteEntity.uuid,
                        QWrongNoteEntity.wrongNoteEntity.id,
                        QWrongNoteEntity.wrongNoteEntity.userId,
                        QWrongNoteEntity.wrongNoteEntity.problemId,
                        QWrongNoteEntity.wrongNoteEntity.wrongAnswer,
                        QWrongNoteEntity.wrongNoteEntity.aiExplanation,
                        QWrongNoteEntity.wrongNoteEntity.isReviewed,
                        QWrongNoteEntity.wrongNoteEntity.nextReviewAt,
                        QWrongNoteEntity.wrongNoteEntity.createdAt,
                        QWrongNoteEntity.wrongNoteEntity.updatedAt
                )
        )
                .from(QWrongNoteEntity.wrongNoteEntity)
                .orderBy(buildOrderBy(sortBy, isAsc ? "asc" : "desc").toArray(new OrderSpecifier[0]))
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    @Override
    public Long countByUserId(Long userId) {
        return queryFactory.select(QWrongNoteEntity.wrongNoteEntity.count())
                .from(QWrongNoteEntity.wrongNoteEntity)
                .where(QWrongNoteEntity.wrongNoteEntity.userId.eq(userId))
                .fetchOne();
    }

    private List<OrderSpecifier<?>> buildOrderBy(String sortBy, String sortDirection) {
        // problem_number, created_at

        if (sortDirection == null || sortDirection.equals("asc")) {
            return switch (sortBy) {
                case "created_at" -> List.of(QWrongNoteEntity.wrongNoteEntity.createdAt.asc());
                case "problem_number" -> List.of(QWrongNoteEntity.wrongNoteEntity.problemId.asc());
                default -> List.of(QWrongNoteEntity.wrongNoteEntity.problemId.asc());
            };
        } else if (sortDirection.equals("desc")) {
            return switch (sortBy) {
                case "created_at" -> List.of(QWrongNoteEntity.wrongNoteEntity.createdAt.desc());
                case "problem_number" -> List.of(QWrongNoteEntity.wrongNoteEntity.problemId.desc());
                default -> List.of(QWrongNoteEntity.wrongNoteEntity.problemId.desc());
            };
        } else {
            throw new IllegalArgumentException("Invalid sort direction: " + sortDirection);
        }

    }

}
