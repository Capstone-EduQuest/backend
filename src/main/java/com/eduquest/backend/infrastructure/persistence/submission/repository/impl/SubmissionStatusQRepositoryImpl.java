package com.eduquest.backend.infrastructure.persistence.submission.repository.impl;

import com.eduquest.backend.infrastructure.persistence.submission.entity.QSubmissionEntity;
import com.eduquest.backend.infrastructure.persistence.submission.entity.QSubmissionStatusEntity;
import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionStatusEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SubmissionStatusQRepositoryImpl implements SubmissionStatusQRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<SubmissionStatusEntity> findBySubmissionUuid(UUID submissionUuid) {

        QSubmissionStatusEntity submissionStatusEntity = QSubmissionStatusEntity.submissionStatusEntity;
        QSubmissionEntity submissionEntity = QSubmissionEntity.submissionEntity;

        return Optional.ofNullable(queryFactory
                .selectFrom(submissionStatusEntity)
                .leftJoin(submissionEntity)
                .on(submissionStatusEntity.submissionId.eq(submissionEntity.id))
                .where(submissionEntity.uuid.eq(submissionUuid))
                .fetchOne());

    }
}
