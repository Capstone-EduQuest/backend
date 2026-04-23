package com.eduquest.backend.infrastructure.persistence.note.repository.impl;

import com.eduquest.backend.domain.note.dto.NoteQuery;
import com.eduquest.backend.infrastructure.persistence.note.entity.QNoteEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NoteQRepositoryImpl implements NoteQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<NoteQuery.Detail> findDetailById(Long id) {
        return Optional.ofNullable(
                queryFactory.select(
                                Projections.constructor(
                                        NoteQuery.Detail.class,
                                        QNoteEntity.noteEntity.uuid,
                                        QNoteEntity.noteEntity.id,
                                        QNoteEntity.noteEntity.userId,
                                        QNoteEntity.noteEntity.title,
                                        QNoteEntity.noteEntity.content,
                                        QNoteEntity.noteEntity.createdAt,
                                        QNoteEntity.noteEntity.updatedAt
                                )
                        )
                        .from(QNoteEntity.noteEntity)
                        .where(QNoteEntity.noteEntity.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public Optional<NoteQuery.Detail> findByUuid(UUID uuid) {
        return Optional.ofNullable(
                queryFactory.select(
                                Projections.constructor(
                                        NoteQuery.Detail.class,
                                        QNoteEntity.noteEntity.uuid,
                                        QNoteEntity.noteEntity.id,
                                        QNoteEntity.noteEntity.userId,
                                        QNoteEntity.noteEntity.title,
                                        QNoteEntity.noteEntity.content,
                                        QNoteEntity.noteEntity.createdAt,
                                        QNoteEntity.noteEntity.updatedAt
                                )
                        )
                        .from(QNoteEntity.noteEntity)
                        .where(QNoteEntity.noteEntity.uuid.eq(uuid))
                        .fetchOne()
        );
    }

    @Override
    public List<NoteQuery.Detail> findByUserId(Long userId, int page, int size, String sortBy, boolean isAsc, String searchBy, String keyword) {
        List<OrderSpecifier<?>> orderSpecifiers = buildOrderBy(sortBy, isAsc ? "asc" : "desc");

        BooleanExpression userPredicate = QNoteEntity.noteEntity.userId.eq(userId);
        BooleanExpression searchPredicate = buildSearchPredicate(searchBy, keyword);

        BooleanExpression wherePredicate = (searchPredicate != null) ? userPredicate.and(searchPredicate) : userPredicate;

        return queryFactory.select(
                        Projections.constructor(
                                NoteQuery.Detail.class,
                                QNoteEntity.noteEntity.uuid,
                                QNoteEntity.noteEntity.id,
                                QNoteEntity.noteEntity.userId,
                                QNoteEntity.noteEntity.title,
                                QNoteEntity.noteEntity.content,
                                QNoteEntity.noteEntity.createdAt,
                                QNoteEntity.noteEntity.updatedAt
                        )
                )
                .from(QNoteEntity.noteEntity)
                .where(wherePredicate)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset((long) Math.max(0, page) * Math.max(1, size))
                .limit(Math.max(1, size))
                .fetch();
    }

    @Override
    public List<NoteQuery.Detail> findByPagination(int page, int size, String sortBy, boolean isAsc, String searchBy, String keyword) {
        List<OrderSpecifier<?>> orderSpecifiers = buildOrderBy(sortBy, isAsc ? "asc" : "desc");

        BooleanExpression searchPredicate = buildSearchPredicate(searchBy, keyword);

        return queryFactory.select(
                        Projections.constructor(
                                NoteQuery.Detail.class,
                                QNoteEntity.noteEntity.uuid,
                                QNoteEntity.noteEntity.id,
                                QNoteEntity.noteEntity.userId,
                                QNoteEntity.noteEntity.title,
                                QNoteEntity.noteEntity.content,
                                QNoteEntity.noteEntity.createdAt,
                                QNoteEntity.noteEntity.updatedAt
                        )
                )
                .from(QNoteEntity.noteEntity)
                .where(searchPredicate)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset((long) Math.max(0, page) * Math.max(1, size))
                .limit(Math.max(1, size))
                .fetch();
    }

    @Override
    public Long countByUserId(Long userId) {
        return queryFactory.select(QNoteEntity.noteEntity.count())
                .from(QNoteEntity.noteEntity)
                .where(QNoteEntity.noteEntity.userId.eq(userId))
                .fetchOne();
    }

    private List<OrderSpecifier<?>> buildOrderBy(String sortBy, String sortDirection) {
        if (sortDirection == null) sortDirection = "asc";
        if (sortBy == null) sortBy = "created_at";

        if (sortDirection.equals("asc")) {
            return switch (sortBy) {
                case "created_at" -> List.of(QNoteEntity.noteEntity.createdAt.asc());
                default -> List.of(QNoteEntity.noteEntity.id.asc());
            };
        } else if (sortDirection.equals("desc")) {
            return switch (sortBy) {
                case "created_at" -> List.of(QNoteEntity.noteEntity.createdAt.desc());
                default -> List.of(QNoteEntity.noteEntity.id.desc());
            };
        } else {
            throw new IllegalArgumentException("Invalid sort direction: " + sortDirection);
        }
    }

    private BooleanExpression buildSearchPredicate(String searchBy, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        if ("title".equals(searchBy)) {
            return QNoteEntity.noteEntity.title.containsIgnoreCase(keyword);
        } else if ("content".equals(searchBy)) {
            return QNoteEntity.noteEntity.content.containsIgnoreCase(keyword);
        } else {
            return QNoteEntity.noteEntity.title.containsIgnoreCase(keyword)
                    .or(QNoteEntity.noteEntity.content.containsIgnoreCase(keyword));
        }
    }

}

