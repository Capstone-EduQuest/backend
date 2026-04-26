package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.model.WrongNote;

import java.util.UUID;

public interface WrongNoteCommandService {

    Long saveOrUpdateWrongNote(WrongNote wrongNote);

    Long createWrongNote(String wrongAnswer, Long userId, Long problemId);

    Long updateWrongNote(WrongNote wrongNote);

    void deleteByUserIdAndProblemId(Long userId, Long problemId);

    void markReviewed(Long userId, Long problemId, boolean isReviewed);

    void deleteByUuid(UUID wrongNoteUuid);

}

