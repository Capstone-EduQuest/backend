package com.eduquest.backend.infrastructure.persistence.submission.repository.impl;

import com.eduquest.backend.domain.submission.dto.WrongNoteQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WrongNoteQRepository {

    Optional<WrongNoteQuery.Detail> findByUserIdAndProblemId(Long userId, Long problemId);

    List<WrongNoteQuery.Detail> findByUserId(Long userId, int page, int size, String sortBy, boolean isAsc);

    Optional<WrongNoteQuery.Detail> findDetailByUuid(UUID wrongNoteUuid);

    List<WrongNoteQuery.Detail> findByPagination(int page, int size, String sortBy, boolean isAsc);

    Long countByUserId(Long userId);

}
