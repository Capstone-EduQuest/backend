package com.eduquest.backend.infrastructure.persistence.note.repository;

import com.eduquest.backend.infrastructure.persistence.note.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteJpaRepository extends JpaRepository<NoteEntity, Long> {

    Optional<NoteEntity> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

}

