
package com.eduquest.backend.infrastructure.persistence.community.repository.impl;

import com.eduquest.backend.domain.community.dto.QuestionQuery;
import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityPostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CommunityPostQRepository {

    Page<CommunityPostEntity> findAllByPagination(Pageable pageable);

    Optional<CommunityPostEntity> findByUuid(UUID uuid);

    Optional<QuestionQuery.Detail> findDetailByUuid(UUID uuid);

    Page<QuestionQuery.Summary> findSummaryByPagination(Pageable pageable);

    Page<QuestionQuery.Summary> findSummaryByPagination(Pageable pageable, String searchBy, String keyword, String sortBy, boolean isAsc);

}

