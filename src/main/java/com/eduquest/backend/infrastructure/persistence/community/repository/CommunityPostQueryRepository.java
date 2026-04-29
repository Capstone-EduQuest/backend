package com.eduquest.backend.infrastructure.persistence.community.repository;

import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityPostEntity;
import com.eduquest.backend.infrastructure.persistence.community.repository.impl.CommunityPostQRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostQueryRepository extends CommunityPostQRepository, JpaRepository<CommunityPostEntity, Long> {
    // QueryDSL 기반 읽기용 기능은 CommunityPostQRepository에 선언합니다.
}

