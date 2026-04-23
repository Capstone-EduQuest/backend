package com.eduquest.backend.infrastructure.persistence.note.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.note.model.Note;
import com.eduquest.backend.domain.note.service.NoteCommandService;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.note.entity.NoteEntity;
import com.eduquest.backend.infrastructure.persistence.note.mapper.NoteEntityMapper;
import com.eduquest.backend.infrastructure.persistence.note.repository.NoteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaNoteCommandService implements NoteCommandService {

    private final NoteJpaRepository noteJpaRepository;
    private final NoteEntityMapper mapper;

    @Override
    @Transactional
    public Long saveNote(Note note) {

        NoteEntity entity = mapper.toEntity(note);
        NoteEntity saved = noteJpaRepository.save(entity);

        return saved.getId();
    }

    @Override
    @Transactional
    public void updateNoteByUuid(UUID uuid, String title, String content) {

        Optional<NoteEntity> existing = noteJpaRepository.findByUuid(uuid);
        if (existing.isPresent()) {
            NoteEntity entity = existing.get();
            entity.update(title, content);
            noteJpaRepository.save(entity);
        } else {
            throw new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA);
        }

    }

    @Override
    @Transactional
    public void deleteByUuid(UUID uuid) {

        Optional<NoteEntity> existing = noteJpaRepository.findByUuid(uuid);
        if (existing.isPresent()) {
            noteJpaRepository.delete(existing.get());
        } else {
            throw new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA);
        }


    }
}

