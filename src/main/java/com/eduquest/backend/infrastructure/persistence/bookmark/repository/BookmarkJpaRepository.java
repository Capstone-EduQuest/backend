package com.eduquest.backend.infrastructure.persistence.bookmark.repository;

import com.eduquest.backend.infrastructure.persistence.bookmark.entity.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookmarkJpaRepository extends JpaRepository<BookmarkEntity, Long> {

    Optional<BookmarkEntity> findByUserIdAndProblemId(Long userId, Long problemId);

    void deleteByUserIdAndProblemId(Long userId, Long problemId);

    Optional<BookmarkEntity> findByUuid(UUID uuid);

}

