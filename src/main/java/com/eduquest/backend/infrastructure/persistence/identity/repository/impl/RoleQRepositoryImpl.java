package com.eduquest.backend.infrastructure.persistence.identity.repository.impl;

import com.eduquest.backend.domain.identity.dto.MemberQuery;
import com.eduquest.backend.infrastructure.persistence.identity.entity.QRoleEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class RoleQRepositoryImpl implements RoleQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberQuery.Role> findAllRoles() {
        return queryFactory.select(
                        Projections.constructor(
                                MemberQuery.Role.class,
                                QRoleEntity.roleEntity.uuid,
                                QRoleEntity.roleEntity.name
                        )
                )
                .from(QRoleEntity.roleEntity)
                .fetch();
    }

    @Override
    public Optional<Long> findIdByUuid(UUID uuid) {
        return Optional.ofNullable(queryFactory.select(QRoleEntity.roleEntity.id)
                .from(QRoleEntity.roleEntity)
                .where(QRoleEntity.roleEntity.uuid.eq(uuid))
                .fetchOne());
    }
}
