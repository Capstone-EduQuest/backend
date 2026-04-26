package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.dto.WrongNoteQuery;
import com.eduquest.backend.domain.submission.model.WrongNote;

import java.util.List;
import java.util.UUID;

public interface WrongNoteQueryService {

    WrongNoteQuery.Detail findWrongDetailNoteByUserIdAndProblemId(Long userId, Long problemId);

    List<WrongNoteQuery.Detail> findWrongDetailNotesByUserId(Long userId, int page, int size, String sortBy, boolean isAsc);

    WrongNoteQuery.Detail findWrongDetailNoteByUuid(UUID wrongNoteUuid);

    WrongNote findWrongNoteByUuid(UUID wrongNoteUuid);

    List<WrongNoteQuery.Detail> findWrongNotes(int page, int size, String sortBy, boolean isAsc);

    long countWrongNotesByUserId(Long userId);

    long countWrongNotes();

}

