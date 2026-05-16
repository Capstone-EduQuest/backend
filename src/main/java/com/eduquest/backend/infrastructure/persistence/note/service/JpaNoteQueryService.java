package com.eduquest.backend.infrastructure.persistence.note.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.note.dto.NoteQuery;
import com.eduquest.backend.domain.note.service.NoteQueryService;
import com.eduquest.backend.infrastructure.persistence.note.exception.NoteDatabaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.note.repository.NoteQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaNoteQueryService implements NoteQueryService {

    private final NoteQueryRepository noteQueryRepository;

    @Override
    public NoteQuery.Detail findNoteByUuid(UUID uuid) {
        return noteQueryRepository.findByUuid(uuid)
                .orElseThrow(() -> new EduQuestException(NoteDatabaseErrorCode.NOTE_NOT_FOUND));
    }

    @Override
    public NoteQuery.Detail findNoteById(Long id) {
        return noteQueryRepository.findDetailById(id)
                .orElseThrow(() -> new EduQuestException(NoteDatabaseErrorCode.NOTE_NOT_FOUND));
    }

    @Override
    public List<NoteQuery.Detail> findNotesByUserId(Long userId, int page, int size, String sortBy, boolean isAsc, String searchBy, String keyword) {
        return noteQueryRepository.findByUserId(userId, page, size, sortBy, isAsc, searchBy, keyword);
    }

    @Override
    public List<NoteQuery.Detail> findNotes(int page, int size, String sortBy, boolean isAsc, String searchBy, String keyword) {
        return noteQueryRepository.findByPagination(page, size, sortBy, isAsc, searchBy, keyword);
    }

    @Override
    public long countNotesByUserId(Long userId) {
        return noteQueryRepository.countByUserId(userId);
    }

    @Override
    public long countNotes() {
        return noteQueryRepository.count();
    }
}

