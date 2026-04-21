package com.eduquest.backend.infrastructure.persistence.community.repository.impl;

import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityPostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CommunityPostQRepository {

    Page<CommunityPostEntity> findAllBy(Pageable pageable);

    Optional<CommunityPostEntity> findByUuid(UUID uuid);

    // 복잡한 조회는 여기에 추가 선언
}

