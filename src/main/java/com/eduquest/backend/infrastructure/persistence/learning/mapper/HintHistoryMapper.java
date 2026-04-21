package com.eduquest.backend.infrastructure.persistence.learning.mapper;

import com.eduquest.backend.domain.learning.model.HintHistory;
import com.eduquest.backend.infrastructure.persistence.learning.entity.HintHistoryEntity;
import org.springframework.stereotype.Component;

@Component
public class HintHistoryMapper {

    public HintHistory toDomain(HintHistoryEntity e) {
        if (e == null) return null;
        return HintHistory.of(e.getId(), e.getCreatedAt(), e.getHintId(), e.getMemberId());
    }

    public HintHistoryEntity toEntity(HintHistory d) {
        if (d == null) return null;
        return HintHistoryEntity.of(d.getMemberId(), d.getHintId());
    }

    public HintHistoryEntity toEntity(Long memberId, Long hintId) {
        return HintHistoryEntity.of(memberId, hintId);
    }

}
