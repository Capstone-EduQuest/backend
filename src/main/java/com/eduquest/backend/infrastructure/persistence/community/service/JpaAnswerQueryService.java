package com.eduquest.backend.infrastructure.persistence.community.service;

import com.eduquest.backend.domain.community.model.Answer;
import com.eduquest.backend.domain.community.service.AnswerQueryService;
import com.eduquest.backend.infrastructure.persistence.community.mapper.CommunityAnswerEntityMapper;
import com.eduquest.backend.infrastructure.persistence.community.repository.CommunityAnswerQueryRepository;
import com.eduquest.backend.infrastructure.persistence.community.repository.CommunityPostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaAnswerQueryService implements AnswerQueryService {

    private final CommunityAnswerQueryRepository answerQueryRepository;
    private final CommunityAnswerEntityMapper answerEntityMapper;
    private final CommunityPostQueryRepository postQueryRepository;

    @Override
    public Answer findAnswerById(Long id) {
        return answerQueryRepository.findById(id).map(answerEntityMapper::toDomain).orElse(null);
    }

    @Override
    public Answer findAnswerByUuid(UUID uuid) {
        return answerQueryRepository.findByUuid(uuid).map(answerEntityMapper::toDomain).orElse(null);
    }

    @Override
    public List<Answer> findAnswersByQuestionId(Long questionId) {
        return answerQueryRepository.findAllByCommunityPostId(questionId).stream()
                .map(answerEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Answer> findAnswersByQuestionUuid(UUID questionUuid) {
        return postQueryRepository.findByUuid(questionUuid)
                .map(post -> answerQueryRepository.findAllByCommunityPostId(post.getId(), PageRequest.of(0, 1000)).getContent().stream()
                        .map(answerEntityMapper::toDomain).collect(Collectors.toList()))
                .orElse(List.of());
    }
}

