package com.eduquest.backend.infrastructure.persistence.community.repository.impl;

import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityAnswerEntity;
import com.eduquest.backend.infrastructure.persistence.community.entity.QCommunityAnswerEntity;
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
public class CommunityAnswerQRepositoryImpl implements CommunityAnswerQRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<CommunityAnswerEntity> findAllByCommunityPostId(Long postId, Pageable pageable) {
		QCommunityAnswerEntity answer = QCommunityAnswerEntity.communityAnswerEntity;

		List<CommunityAnswerEntity> content = queryFactory
				.selectFrom(answer)
				.where(answer.communityPostId.eq(postId))
				.orderBy(answer.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total = queryFactory.select(answer.count()).from(answer).where(answer.communityPostId.eq(postId)).fetchOne();
		if (total == null) total = 0L;

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Optional<CommunityAnswerEntity> findByUuid(UUID uuid) {
		QCommunityAnswerEntity answer = QCommunityAnswerEntity.communityAnswerEntity;
		CommunityAnswerEntity entity = queryFactory.selectFrom(answer).where(answer.uuid.eq(uuid)).fetchOne();
		return Optional.ofNullable(entity);
	}

	@Override
	public List<CommunityAnswerEntity> findAllByCommunityPostId(Long postId) {
		QCommunityAnswerEntity answer = QCommunityAnswerEntity.communityAnswerEntity;
		return queryFactory.selectFrom(answer).where(answer.communityPostId.eq(postId)).fetch();
	}
}

