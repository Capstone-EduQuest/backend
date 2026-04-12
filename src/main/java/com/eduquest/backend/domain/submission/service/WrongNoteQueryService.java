package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.dto.WrongNoteDto;

import java.util.List;

public interface WrongNoteQueryService {

    WrongNoteDto.Detail findWrongNoteByUserIdAndProblemId(Long userId, Long problemId);

    List<WrongNoteDto.Detail> findWrongNotesByUserId(Long userId, int page, int size, String sortBy, boolean isAsc);

    long countWrongNotesByUserId(Long userId);

    // UUID 기반 조회
    WrongNoteDto.Detail findWrongNoteByUuid(java.util.UUID wrongNoteUuid);

    // 전체(글로벌) 목록 및 카운트
    List<WrongNoteDto.Detail> findWrongNotes(int page, int size, String sortBy, boolean isAsc);

    long countWrongNotes();

}

