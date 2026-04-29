package com.eduquest.backend.infrastructure.persistence.identity.repository.impl;

import com.eduquest.backend.domain.identity.dto.MemberQuery;
import com.eduquest.backend.domain.identity.dto.UserDetailsData;
import com.eduquest.backend.infrastructure.persistence.identity.entity.QMemberEntity;
import com.eduquest.backend.infrastructure.persistence.identity.entity.QRoleEntity;
import com.eduquest.backend.infrastructure.persistence.identity.entity.QUserRoleEntity;
import com.eduquest.backend.infrastructure.persistence.reward.entity.QWalletEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MemberQRepositoryImpl implements MemberQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByEmail(String email) {
        return queryFactory.selectOne()
                .from(QMemberEntity.memberEntity)
                .where(QMemberEntity.memberEntity.email.eq(email))
                .fetchFirst() != null;
    }

    @Override
    public Optional<UserDetailsData> findUserDetailsByUserId(String userId) {
        return Optional.ofNullable(queryFactory.select(
                        Projections.constructor(
                                UserDetailsData.class,
                                QMemberEntity.memberEntity.userId,
                                QMemberEntity.memberEntity.password,
                                QRoleEntity.roleEntity.name,
                                QMemberEntity.memberEntity.isLocked
                        )
                )
                .from(QMemberEntity.memberEntity)
                .join(QUserRoleEntity.userRoleEntity)
                .on(QUserRoleEntity.userRoleEntity.member.id.eq(QMemberEntity.memberEntity.id))
                .join(QUserRoleEntity.userRoleEntity.role, QRoleEntity.roleEntity)
                .where(QMemberEntity.memberEntity.userId.eq(userId))
                .fetchOne());
    }

    @Override
    public Optional<MemberQuery.EmailAndUserId> findEmailAndUserIdByEmail(String email) {
        return Optional.ofNullable(
                queryFactory.select(
                                Projections.constructor(
                                        MemberQuery.EmailAndUserId.class,
                                        QMemberEntity.memberEntity.email,
                                        QMemberEntity.memberEntity.userId
                                )
                        )
                        .from(QMemberEntity.memberEntity)
                        .where(QMemberEntity.memberEntity.email.eq(email))
                        .fetchOne()
        );
    }

    @Override
    public Optional<UUID> findUuidByUserId(String userId) {
        return Optional.ofNullable(
                queryFactory.select(QMemberEntity.memberEntity.uuid)
                        .from(QMemberEntity.memberEntity)
                        .where(QMemberEntity.memberEntity.userId.eq(userId))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Long> findIdByUuid(UUID uuid) {
        return Optional.ofNullable(
                queryFactory.select(QMemberEntity.memberEntity.id)
                        .from(QMemberEntity.memberEntity)
                        .where(QMemberEntity.memberEntity.uuid.eq(uuid))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Long> findIdByUserId(String userId) {
        return Optional.ofNullable(
                queryFactory.select(QMemberEntity.memberEntity.id)
                        .from(QMemberEntity.memberEntity)
                        .where(QMemberEntity.memberEntity.userId.eq(userId))
                        .fetchOne()
        );
    }

    @Override
    public Optional<MemberQuery.UserProfile> findUserProfileByUuid(UUID uuid) {
        return Optional.ofNullable(
                queryFactory.select(
                                Projections.constructor(
                                        MemberQuery.UserProfile.class,
                                        QMemberEntity.memberEntity.uuid,
                                        QMemberEntity.memberEntity.userId,
                                        QMemberEntity.memberEntity.birth,
                                        QMemberEntity.memberEntity.nickname,
                                        QWalletEntity.walletEntity.balance,
                                        QRoleEntity.roleEntity.name,
                                        QMemberEntity.memberEntity.isLocked,
                                        QMemberEntity.memberEntity.profileId
                                )
                        )
                        .from(QMemberEntity.memberEntity)
                        .leftJoin(QUserRoleEntity.userRoleEntity)
                        .on(QUserRoleEntity.userRoleEntity.member.id.eq(QMemberEntity.memberEntity.id))
                        .leftJoin(QUserRoleEntity.userRoleEntity.role, QRoleEntity.roleEntity)
                        .on(QUserRoleEntity.userRoleEntity.member.id.eq(QMemberEntity.memberEntity.id))
                        .leftJoin(QWalletEntity.walletEntity)
                        .on(QWalletEntity.walletEntity.userId.eq(QMemberEntity.memberEntity.id))
                        .where(QMemberEntity.memberEntity.uuid.eq(uuid))
                        .fetchOne()
        );
    }

    @Override
    public List<MemberQuery.UserListResult> findAllMembersByPagination(int page, int size, String sortBy, String sortDirection) {
        return queryFactory.select(
                        Projections.constructor(
                                MemberQuery.UserListResult.class,
                                QMemberEntity.memberEntity.uuid,
                                QMemberEntity.memberEntity.userId,
                                QMemberEntity.memberEntity.email,
                                QMemberEntity.memberEntity.nickname
                        )
                )
                .from(QMemberEntity.memberEntity)
                .join(QUserRoleEntity.userRoleEntity)
                .on(QUserRoleEntity.userRoleEntity.member.id.eq(QMemberEntity.memberEntity.id))
                .join(QUserRoleEntity.userRoleEntity.role, QRoleEntity.roleEntity)
                .orderBy(buildOrderBy(sortBy, sortDirection).toArray(new OrderSpecifier[0]))
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    private List<OrderSpecifier<?>> buildOrderBy(String sortBy, String sortDirection) {

        if (sortDirection == null || sortDirection.equals("asc")) {
            return switch (sortBy) {
                case "balance" -> List.of(Expressions.numberPath(Long.class, "balance").asc());
                default -> List.of(QMemberEntity.memberEntity.userId.asc());
            };
        } else if (sortDirection.equals("desc")) {
            return switch (sortBy) {
                case "balance" -> List.of(Expressions.numberPath(Long.class, "balance").desc());
                default -> List.of(QMemberEntity.memberEntity.userId.desc());
            };
        } else {
            throw new IllegalArgumentException("Invalid sort direction: " + sortDirection);
        }
    }

}
