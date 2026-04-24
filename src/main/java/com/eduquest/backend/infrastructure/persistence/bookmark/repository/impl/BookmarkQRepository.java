package com.eduquest.backend.infrastructure.persistence.bookmark.repository.impl;

import com.eduquest.backend.domain.bookmark.dto.BookmarkQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookmarkQRepository {

    Optional<Long> findProblemIdByUuid(UUID problemUuid);

    List<BookmarkQuery.Summary> findByUserId(Long userId, int page, int size, String sortBy, boolean isAsc);

    List<BookmarkQuery.Summary> findByPagination(int page, int size, String sortBy, boolean isAsc);

    Long countByUserId(Long userId);

}

