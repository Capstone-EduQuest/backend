package com.eduquest.backend.infrastructure.persistence.submission.repository;

import com.eduquest.backend.infrastructure.persistence.submission.entity.WrongNoteEntity;
import com.eduquest.backend.infrastructure.persistence.submission.repository.impl.WrongNoteQRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WrongNoteQueryRepository extends JpaRepository<WrongNoteEntity, Long>, WrongNoteQRepository {

}
