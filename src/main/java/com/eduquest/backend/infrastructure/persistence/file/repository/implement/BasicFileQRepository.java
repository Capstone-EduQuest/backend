package com.eduquest.backend.infrastructure.persistence.file.repository.implement;

import com.eduquest.backend.infrastructure.persistence.file.entity.QFileEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Querydsl Repository 구현체
@Repository
@RequiredArgsConstructor
public class BasicFileQRepository implements FileQRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> findIdByStoredName(String storedName) {

        return Optional.ofNullable(queryFactory.selectFrom(QFileEntity.fileEntity)
                .where(QFileEntity.fileEntity.storedName.eq(storedName))
                .select(QFileEntity.fileEntity.id)
                .fetchOne());

    }

    @Override
    public Optional<String> findStoredNameById(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(QFileEntity.fileEntity)
                .where(QFileEntity.fileEntity.id.eq(id))
                .select(QFileEntity.fileEntity.storedName)
                .fetchOne());
    }
}
