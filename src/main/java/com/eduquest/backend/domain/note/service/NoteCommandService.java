package com.eduquest.backend.domain.note.service;

import com.eduquest.backend.domain.note.model.Note;

import java.util.UUID;

public interface NoteCommandService {

    Long saveNote(Note note);

    void updateNoteByUuid(UUID uuid, String title, String content);

    void deleteByUuid(UUID uuid);

}

