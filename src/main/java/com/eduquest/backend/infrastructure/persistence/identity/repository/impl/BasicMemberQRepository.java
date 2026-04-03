package com.eduquest.backend.infrastructure.persistence.identity.repository.impl;

import com.eduquest.backend.domain.member.dto.UserDetailsData;
import com.eduquest.backend.infrastructure.persistence.identity.entity.QMemberEntity;
import com.eduquest.backend.infrastructure.persistence.identity.entity.QRoleEntity;
import com.eduquest.backend.infrastructure.persistence.identity.entity.QUserRoleEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BasicMemberQRepository implements MemberQRepository {

    private final JPAQueryFactory queryFactory;


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
                .join(QUserRoleEntity.userRoleEntity.role)
                .fetchJoin()
                .where(QMemberEntity.memberEntity.userId.eq(userId))
                .fetchOne());
    }
}
