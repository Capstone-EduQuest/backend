package com.eduquest.backend.domain.note.service;

import com.eduquest.backend.domain.note.dto.NoteQuery;

import java.util.List;
import java.util.UUID;

public interface NoteQueryService {

    NoteQuery.Detail findNoteByUuid(UUID uuid);

    NoteQuery.Detail findNoteById(Long id);

    List<NoteQuery.Detail> findNotesByUserId(Long userId, int page, int size, String sortBy, boolean isAsc, String searchBy, String keyword);

    List<NoteQuery.Detail> findNotes(int page, int size, String sortBy, boolean isAsc, String searchBy, String keyword);

    long countNotesByUserId(Long userId);

    long countNotes();

}

