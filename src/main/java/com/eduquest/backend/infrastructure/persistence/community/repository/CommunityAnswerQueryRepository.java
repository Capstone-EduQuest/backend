package com.eduquest.backend.infrastructure.persistence.community.repository;

import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityAnswerEntity;
import com.eduquest.backend.infrastructure.persistence.community.repository.impl.CommunityAnswerQRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityAnswerQueryRepository extends CommunityAnswerQRepository, JpaRepository<CommunityAnswerEntity, Long> {
    // QueryDSL 기반 읽기용 기능은 CommunityAnswerQRepository에 선언합니다.
}

