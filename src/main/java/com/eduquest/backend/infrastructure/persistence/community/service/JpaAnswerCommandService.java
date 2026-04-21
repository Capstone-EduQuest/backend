package com.eduquest.backend.infrastructure.persistence.community.service;

import com.eduquest.backend.domain.community.model.Answer;
import com.eduquest.backend.domain.community.service.AnswerCommandService;
import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityAnswerEntity;
import com.eduquest.backend.infrastructure.persistence.community.mapper.CommunityAnswerEntityMapper;
import com.eduquest.backend.infrastructure.persistence.community.repository.CommunityAnswerJpaRepository;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaAnswerCommandService implements AnswerCommandService {

    private final CommunityAnswerJpaRepository answerJpaRepository;
    private final CommunityAnswerEntityMapper answerEntityMapper;

    @Transactional
    @Override
    public Long saveAnswer(Answer answer) {
        CommunityAnswerEntity entity = answerEntityMapper.toEntity(answer);
        entity = answerJpaRepository.save(entity);
        return entity.getId();
    }

    @Transactional
    @Override
    public void deleteAnswerByUuid(UUID uuid) {
        answerJpaRepository.findByUuid(uuid).ifPresentOrElse(e -> answerJpaRepository.delete(e),
                () -> { throw new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA); });
    }

    @Transactional
    @Override
    public void adoptAnswerByUuid(UUID answerUuid) {
        Optional<CommunityAnswerEntity> maybe = answerJpaRepository.findByUuid(answerUuid);
        if (maybe.isEmpty()) {
            throw new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA);
        }

        CommunityAnswerEntity answer = maybe.get();
        answer.adopt();
        answerJpaRepository.save(answer);
    }
}

