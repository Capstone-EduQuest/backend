package com.eduquest.backend.infrastructure.persistence.note.repository.impl;

import com.eduquest.backend.domain.note.dto.NoteQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteQRepository {

    Optional<NoteQuery.Detail> findDetailById(Long id);

    Optional<NoteQuery.Detail> findByUuid(UUID uuid);

    List<NoteQuery.Detail> findByUserId(Long userId, int page, int size, String sortBy, boolean isAsc, String searchBy, String keyword);

    List<NoteQuery.Detail> findByPagination(int page, int size, String sortBy, boolean isAsc, String searchBy, String keyword);

    Long countByUserId(Long userId);

}

