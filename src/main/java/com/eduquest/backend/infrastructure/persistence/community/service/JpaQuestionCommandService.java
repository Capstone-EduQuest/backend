package com.eduquest.backend.infrastructure.persistence.community.service;

import com.eduquest.backend.domain.community.model.Question;
import com.eduquest.backend.domain.community.service.QuestionCommandService;
import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityPostEntity;
import com.eduquest.backend.infrastructure.persistence.community.mapper.CommunityPostEntityMapper;
import com.eduquest.backend.infrastructure.persistence.community.repository.CommunityPostJpaRepository;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaQuestionCommandService implements QuestionCommandService {

    private final CommunityPostJpaRepository postJpaRepository;
    private final CommunityPostEntityMapper postEntityMapper;

    @Transactional
    @Override
    public Long saveQuestion(Question question) {
        CommunityPostEntity entity = postEntityMapper.toEntity(question);
        entity = postJpaRepository.save(entity);
        return entity.getId();
    }

    @Transactional
    @Override
    public void deleteQuestionByUuid(UUID uuid) {
        postJpaRepository.findByUuid(uuid).ifPresentOrElse(e -> postJpaRepository.delete(e),
                () -> { throw new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA); });
    }

    @Transactional
    @Override
    public void markAdoptedByUuid(UUID questionUuid, UUID answerUuid) {
        Optional<CommunityPostEntity> maybe = postJpaRepository.findByUuid(questionUuid);
        if (maybe.isEmpty()) {
            throw new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA);
        }

        CommunityPostEntity post = maybe.get();
        post.adopt();
        postJpaRepository.save(post);
    }
}

