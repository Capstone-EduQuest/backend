package com.eduquest.backend.infrastructure.persistence.community.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.community.dto.QuestionQuery;
import com.eduquest.backend.domain.community.model.Question;
import com.eduquest.backend.domain.community.service.QuestionQueryService;
import com.eduquest.backend.infrastructure.persistence.community.exception.CommunityDatabaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.community.mapper.CommunityPostEntityMapper;
import com.eduquest.backend.infrastructure.persistence.community.repository.CommunityPostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaQuestionQueryService implements QuestionQueryService {

	private final CommunityPostQueryRepository postQueryRepository;
	private final CommunityPostEntityMapper postEntityMapper;

	@Override
	public Question findQuestionById(Long id) {
		return postQueryRepository.findById(id).map(postEntityMapper::toDomain)
				.orElseThrow(() -> new EduQuestException(CommunityDatabaseErrorCode.QUESTION_NOT_FOUND));
	}

	@Override
	public Question findQuestionByUuid(UUID uuid) {
		return postQueryRepository.findByUuid(uuid).map(postEntityMapper::toDomain)
				.orElseThrow(() -> new EduQuestException(CommunityDatabaseErrorCode.QUESTION_NOT_FOUND));
	}

	@Override
	public QuestionQuery.Detail findQuestionDetailByUuid(UUID uuid) {
		return postQueryRepository.findDetailByUuid(uuid)
				.orElseThrow(() -> new EduQuestException(CommunityDatabaseErrorCode.QUESTION_NOT_FOUND));
	}

	@Override
	public List<Question> findQuestionsByUserId(Long userId) {
		return postQueryRepository.findAllByPagination(PageRequest.of(0, 1000)).getContent().stream()
				.filter(p -> p.getUserId().equals(userId))
				.map(postEntityMapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public List<QuestionQuery.Summary> findAll(int page, int size, String sortBy, boolean isAsc, String searchBy, String keyword) {
		return postQueryRepository.findSummaryByPagination(PageRequest.of(page, size), searchBy, keyword, sortBy, isAsc).getContent();
	}

}


