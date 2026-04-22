package com.eduquest.backend.infrastructure.persistence.community.repository;

import com.eduquest.backend.infrastructure.persistence.community.entity.CommunityAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommunityAnswerJpaRepository extends JpaRepository<CommunityAnswerEntity, Long> {

    Optional<CommunityAnswerEntity> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    List<CommunityAnswerEntity> findAllByCommunityPostId(Long communityPostId);
}

