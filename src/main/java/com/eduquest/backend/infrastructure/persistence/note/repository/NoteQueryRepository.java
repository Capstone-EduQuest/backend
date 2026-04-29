package com.eduquest.backend.infrastructure.persistence.note.repository;

import com.eduquest.backend.infrastructure.persistence.note.entity.NoteEntity;
import com.eduquest.backend.infrastructure.persistence.note.repository.impl.NoteQRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteQueryRepository extends NoteQRepository, JpaRepository<NoteEntity, Long> {

}

