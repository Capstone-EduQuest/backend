package com.eduquest.backend.infrastructure.persistence.note.mapper;

import com.eduquest.backend.domain.note.model.Note;
import com.eduquest.backend.infrastructure.persistence.note.entity.NoteEntity;
import org.springframework.stereotype.Component;

@Component
public class NoteEntityMapper {

    public NoteEntity toEntity(Long userId, String title, String content) {
        return NoteEntity.of(title, content, userId);
    }

    public NoteEntity toEntity(Note note) {
        if (note == null) return null;
        return NoteEntity.of(note.getTitle(), note.getContent(), note.getUserId());
    }

    public Note toDomain(NoteEntity entity) {
        if (entity == null) return null;
        return Note.of(
                entity.getUuid(),
                entity.getId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}

