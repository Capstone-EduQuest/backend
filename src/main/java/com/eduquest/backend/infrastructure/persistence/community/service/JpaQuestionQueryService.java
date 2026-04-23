package com.eduquest.backend.infrastructure.persistence.community.service;

import com.eduquest.backend.domain.community.dto.QuestionQuery;
import com.eduquest.backend.domain.community.model.Question;
import com.eduquest.backend.domain.community.service.QuestionQueryService;
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
		return postQueryRepository.findById(id).map(postEntityMapper::toDomain).orElse(null);
	}

	@Override
	public Question findQuestionByUuid(UUID uuid) {
		return postQueryRepository.findByUuid(uuid).map(postEntityMapper::toDomain).orElse(null);
	}

	@Override
	public List<Question> findQuestionsByUserId(Long userId) {
		return postQueryRepository.findAllBy(PageRequest.of(0, 1000)).getContent().stream()
				.filter(p -> p.getUserId().equals(userId))
				.map(postEntityMapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public List<QuestionQuery.Summary> findAll(int page, int size, String sortBy, boolean isAsc, String searchBy, String keyword) {
		return postQueryRepository.findSummaryBy(PageRequest.of(page, size), searchBy, keyword, sortBy, isAsc).getContent();
	}

}


