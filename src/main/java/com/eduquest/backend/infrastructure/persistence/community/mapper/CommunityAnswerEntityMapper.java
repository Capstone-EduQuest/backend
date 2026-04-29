package com.eduquest.backend.infrastructure.persistence.community.mapper;

import com.eduquest.backend.domain.community.model.Answer;
import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityAnswerEntity;
import org.springframework.stereotype.Component;

@Component
public class CommunityAnswerEntityMapper {

    public CommunityAnswerEntity toEntity(Answer answer) {
        if (answer == null) return null;
        return CommunityAnswerEntity.of(answer.getContent(), answer.getUserId(), answer.getCommunityPostId());
    }

    public CommunityAnswerEntity toEntityWithIdAndUuid(Answer answer) {
        if (answer == null) return null;
        CommunityAnswerEntity entity = toEntity(answer);
        if (answer.getUuid() != null) {
            entity.setUuid(answer.getUuid());
        }
        if (answer.getId() != null) {
            entity.setId(answer.getId());
        }
        return entity;
    }

    public Answer toDomain(CommunityAnswerEntity entity) {
        if (entity == null) return null;
        return Answer.of(entity.getId(), entity.getUuid(), entity.getContent(), entity.getIsAdopted(), entity.getCreatedAt(), entity.getUserId(), entity.getCommunityPostId());
    }
}

