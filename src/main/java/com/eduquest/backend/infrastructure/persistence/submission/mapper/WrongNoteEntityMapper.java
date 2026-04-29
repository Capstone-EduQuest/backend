package com.eduquest.backend.infrastructure.persistence.submission.mapper;

import com.eduquest.backend.domain.submission.model.WrongNote;
import com.eduquest.backend.infrastructure.persistence.submission.entity.WrongNoteEntity;
import org.springframework.stereotype.Component;

@Component
public class WrongNoteEntityMapper {

    public WrongNoteEntity toEntity(Long userId, Long problemId, String wrongAnswer, String aiExplanation) {
        return WrongNoteEntity.of(wrongAnswer, aiExplanation, Boolean.FALSE, null, userId, problemId);
    }

    public WrongNoteEntity toEntity(WrongNote wn) {
        if (wn == null) return null;
        return WrongNoteEntity.of(
                wn.getWrongAnswer(),
                wn.getAiExplanation(),
                wn.getIsReviewed(),
                wn.getNextReviewAt(),
                wn.getUserId(),
                wn.getProblemId()
        );
    }

    public WrongNote toDomain(WrongNoteEntity e) {
        if (e == null) return null;
        return WrongNote.of(
                e.getUuid(),
                e.getId(),
                e.getUserId(),
                e.getProblemId(),
                e.getWrongAnswer(),
                e.getAiExplanation(),
                e.getIsReviewed(),
                e.getNextReviewAt(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

}

