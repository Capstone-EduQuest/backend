package com.eduquest.backend.infrastructure.persistence.submission.repository;

import com.eduquest.backend.infrastructure.persistence.submission.entity.WrongNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WrongNoteJpaRepository extends JpaRepository<WrongNoteEntity, Long> {

    Optional<WrongNoteEntity> findByUserIdAndProblemId(Long userId, Long problemId);

    void deleteByUserIdAndProblemId(Long userId, Long problemId);

    void deleteByUuid(UUID uuid);

}

