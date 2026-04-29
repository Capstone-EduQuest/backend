package com.eduquest.backend.application.note.service;

import com.eduquest.backend.application.note.dto.CreateNoteCommand;
import com.eduquest.backend.application.note.dto.NoteDto;
import com.eduquest.backend.application.note.dto.NoteListResult;
import com.eduquest.backend.application.note.dto.UpdateNoteCommand;
import com.eduquest.backend.application.note.exception.NoteErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.identity.model.Member;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.domain.note.dto.NoteQuery;
import com.eduquest.backend.domain.note.model.Note;
import com.eduquest.backend.domain.note.service.NoteCommandService;
import com.eduquest.backend.domain.note.service.NoteQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final MemberQueryService memberQueryService;
    private final NoteCommandService noteCommandService;
    private final NoteQueryService noteQueryService;

    @Transactional
    public UUID createNote(String userId, CreateNoteCommand command) {
        if (userId == null || userId.isBlank()) {
            throw new EduQuestException(NoteErrorCode.INVALID_REQUEST);
        }

        Long memberId = memberQueryService.findMemberIdByUserId(userId);

        Note note = Note.of(memberId, command.title(), command.content());

        Long savedId = noteCommandService.saveNote(note);

        NoteQuery.Detail detail = noteQueryService.findNoteById(savedId);

        if (detail == null) {
            throw new EduQuestException(NoteErrorCode.INVALID_REQUEST);
        }

        return detail.uuid();
    }

    @Transactional(readOnly = true)
    public NoteDto findNoteDtoByUuid(UUID uuid) {
        NoteQuery.Detail detail = noteQueryService.findNoteByUuid(uuid);
        if (detail == null) {
            throw new EduQuestException(NoteErrorCode.NOT_FOUND);
        }
        Member member = memberQueryService.findMemberById(detail.userId());
        return NoteDto.of(detail.uuid(), detail.id(), member.getUuid(), detail.title(), detail.content(), detail.createdAt(), detail.updatedAt());
    }

    @Transactional(readOnly = true)
    public NoteListResult findNotes(int page, int size, String sort, Boolean isAsc, String searchBy, String keyword) {
        List<NoteQuery.Detail> details = noteQueryService.findNotes(page, size, sort, isAsc != null && isAsc, searchBy, keyword);
        long total = noteQueryService.countNotes();

        List<NoteListResult.Item> items = details.stream().map(d -> {
            Member member = memberQueryService.findMemberById(d.userId());
            return NoteListResult.Item.of(d.uuid(), d.id(), member.getUuid(), d.title(), d.content(), d.createdAt(), d.updatedAt());
        }).collect(Collectors.toList());

        return NoteListResult.of(page, size, sort, isAsc, total, items);
    }

    @Transactional(readOnly = true)
    public NoteListResult findNotesByUserUuid(UUID userUuid, int page, int size, String sort, Boolean isAsc, String searchBy, String keyword) {
        Long memberId = memberQueryService.findMemberIdByUuid(userUuid);
        List<NoteQuery.Detail> details = noteQueryService.findNotesByUserId(memberId, page, size, sort, isAsc != null && isAsc, searchBy, keyword);
        long total = noteQueryService.countNotesByUserId(memberId);

        List<NoteListResult.Item> items = details.stream().map(d -> {
            Member member = memberQueryService.findMemberById(d.userId());
            return NoteListResult.Item.of(d.uuid(), d.id(), member.getUuid(), d.title(), d.content(), d.createdAt(), d.updatedAt());
        }).collect(Collectors.toList());

        return NoteListResult.of(page, size, sort, isAsc, total, items);
    }

    @Transactional
    public void updateNoteByUuid(UUID uuid, String userId, UpdateNoteCommand command) {

        if (userId == null || userId.isBlank()) {
            throw new EduQuestException(NoteErrorCode.INVALID_REQUEST);
        }

        Long memberId = memberQueryService.findMemberIdByUserId(userId);
        NoteQuery.Detail detail = noteQueryService.findNoteByUuid(uuid);
        if (detail == null) {
            throw new EduQuestException(NoteErrorCode.NOT_FOUND);
        }

        if (!detail.userId().equals(memberId)) {
            throw new EduQuestException(NoteErrorCode.FORBIDDEN);
        }

        noteCommandService.updateNoteByUuid(uuid, command.title(), command.content());
    }

    @Transactional
    public void deleteNoteByUuid(UUID uuid, String userId) {

        if (userId == null || userId.isBlank()) {
            throw new EduQuestException(NoteErrorCode.INVALID_REQUEST);
        }

        Long memberId = memberQueryService.findMemberIdByUserId(userId);
        NoteQuery.Detail detail = noteQueryService.findNoteByUuid(uuid);
        if (detail == null) {
            throw new EduQuestException(NoteErrorCode.NOT_FOUND);
        }

        if (!detail.userId().equals(memberId)) {
            throw new EduQuestException(NoteErrorCode.FORBIDDEN);
        }

        noteCommandService.deleteByUuid(uuid);

    }

}

