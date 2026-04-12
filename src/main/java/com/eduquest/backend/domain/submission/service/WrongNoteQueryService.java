package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.dto.WrongNoteQuery;

import java.util.List;
import java.util.UUID;

public interface WrongNoteQueryService {

    WrongNoteQuery.Detail findWrongNoteByUserIdAndProblemId(Long userId, Long problemId);

    List<WrongNoteQuery.Detail> findWrongNotesByUserId(Long userId, int page, int size, String sortBy, boolean isAsc);

    WrongNoteQuery.Detail findWrongNoteByUuid(UUID wrongNoteUuid);

    List<WrongNoteQuery.Detail> findWrongNotes(int page, int size, String sortBy, boolean isAsc);

    long countWrongNotesByUserId(Long userId);

    long countWrongNotes();

}

