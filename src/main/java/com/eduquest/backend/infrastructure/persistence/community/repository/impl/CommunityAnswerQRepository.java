package com.eduquest.backend.infrastructure.persistence.community.repository.impl;

import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityAnswerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommunityAnswerQRepository {

    Page<CommunityAnswerEntity> findAllByCommunityPostId(Long postId, Pageable pageable);

    Optional<CommunityAnswerEntity> findByUuid(UUID uuid);

    List<CommunityAnswerEntity> findAllByCommunityPostId(Long postId);
}

