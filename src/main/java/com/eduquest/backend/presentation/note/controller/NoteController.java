package com.eduquest.backend.presentation.note.controller;

import com.eduquest.backend.application.note.dto.CreateNoteCommand;
import com.eduquest.backend.application.note.dto.NoteDto;
import com.eduquest.backend.application.note.dto.NoteListResult;
import com.eduquest.backend.application.note.dto.UpdateNoteCommand;
import com.eduquest.backend.application.note.service.NoteService;
import com.eduquest.backend.presentation.note.dto.request.NoteCreateRequest;
import com.eduquest.backend.presentation.note.dto.request.NoteListRequest;
import com.eduquest.backend.presentation.note.dto.request.NoteUpdateRequest;
import com.eduquest.backend.presentation.note.dto.response.NoteListResponse;
import com.eduquest.backend.presentation.note.dto.response.NoteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<UUID> createNote(
            @Valid @RequestBody NoteCreateRequest request,
            Authentication authentication
    ) {
        String username = authentication.getName();
        UUID createdUuid = noteService.createNote(username, CreateNoteCommand.of(request.title(), request.content()));
        return ResponseEntity.ok(createdUuid);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{uuid}")
    public ResponseEntity<NoteResponse> getNote(
            @PathVariable UUID uuid,
            Authentication authentication
    ) {
        NoteDto dto = noteService.findNoteDtoByUuid(uuid);
        NoteResponse response = NoteResponse.of(dto.uuid(), dto.title(), dto.content(), dto.authorUuid(), dto.createdAt(), dto.updatedAt());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<NoteListResponse.NoteList> listNotes(
            @Valid @ModelAttribute NoteListRequest request,
            Authentication authentication
    ) {
        NoteListResult result = noteService.findNotes(request.page(), request.size(), request.sort(), request.isAsc(), request.searchBy(), request.keyword());

        var items = result.results().stream().map(i -> com.eduquest.backend.presentation.note.dto.response.NoteResponse.of(i.uuid(), i.title(), i.content(), i.authorUuid(), i.createdAt(), i.updatedAt())).toList();

        return ResponseEntity.ok(NoteListResponse.NoteList.of(result.page(), result.size(), result.sort(), result.isAsc() == null ? false : result.isAsc(), result.total(), items));
    }

    @PreAuthorize("@authz.isSelfByUuid(authentication, #uuid) or hasRole('ADMIN')")
    @GetMapping("/users/{uuid}")
    public ResponseEntity<NoteListResponse.NoteList> listByUser(
            @PathVariable UUID uuid,
            @Valid @ModelAttribute NoteListRequest request,
            Authentication authentication
    ) {
        NoteListResult result = noteService.findNotesByUserUuid(uuid, request.page(), request.size(), request.sort(), request.isAsc(), request.searchBy(), request.keyword());

        var items = result.results().stream().map(i -> com.eduquest.backend.presentation.note.dto.response.NoteResponse.of(i.uuid(), i.title(), i.content(), i.authorUuid(), i.createdAt(), i.updatedAt())).toList();

        return ResponseEntity.ok(NoteListResponse.NoteList.of(result.page(), result.size(), result.sort(), result.isAsc() == null ? false : result.isAsc(), result.total(), items));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{uuid}")
    public ResponseEntity<Void> updateNote(
            @PathVariable UUID uuid,
            @Valid @RequestBody NoteUpdateRequest request,
            Authentication authentication
    ) {
        String username = authentication.getName();
        noteService.updateNoteByUuid(uuid, username, UpdateNoteCommand.of(request.title(), request.content()));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable UUID uuid,
            Authentication authentication
    ) {
        String username = authentication.getName();
        noteService.deleteNoteByUuid(uuid, username);
        return ResponseEntity.noContent().build();
    }
}

