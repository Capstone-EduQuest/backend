package com.eduquest.backend.infrastructure.persistence.community.mapper;

import com.eduquest.backend.domain.community.model.Question;
import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityPostEntity;
import org.springframework.stereotype.Component;

@Component
public class CommunityPostEntityMapper {

    public CommunityPostEntity toEntity(Question question) {
        if (question == null) return null;
        return CommunityPostEntity.of(question.getTitle(), question.getContent(), question.getUserId());
    }

    public CommunityPostEntity toEntityWithIdAndUuid(Question question) {
        if (question == null) return null;
        CommunityPostEntity entity = toEntity(question);
        if (question.getUuid() != null) {
            entity.setUuid(question.getUuid());
        }
        if (question.getId() != null) {
            entity.setId(question.getId());
        }
        return entity;
    }

    public Question toDomain(CommunityPostEntity entity) {
        if (entity == null) return null;
        return Question.of(
                entity.getId(),
                entity.getUuid(),
                entity.getTitle(),
                entity.getContent(),
                entity.getIsAdopted(),
                entity.getCreatedAt(),
                entity.getUserId()
        );
    }
}

