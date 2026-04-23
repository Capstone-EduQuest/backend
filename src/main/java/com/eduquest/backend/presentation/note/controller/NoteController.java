package com.eduquest.backend.presentation.note.controller;

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
        UUID createdUuid = noteService.createNote(username, request);
        return ResponseEntity.ok(createdUuid);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{uuid}")
    public ResponseEntity<NoteResponse> getNote(
            @PathVariable UUID uuid,
            Authentication authentication
    ) {
        NoteResponse response = noteService.findNoteByUuid(uuid);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<NoteListResponse.NoteList> listNotes(
            @Valid @ModelAttribute NoteListRequest request,
            Authentication authentication
    ) {
        NoteListResponse.NoteList list = noteService.findNotes(
                request.page(),
                request.size(),
                request.sort(),
                request.isAsc(),
                request.searchBy(),
                request.keyword()
        );
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("@authz.isSelfByUuid(authentication, #uuid) or hasRole('ADMIN')")
    @GetMapping("/users/{uuid}")
    public ResponseEntity<NoteListResponse.NoteList> listByUser(
            @PathVariable UUID uuid,
            @Valid @ModelAttribute NoteListRequest request,
            Authentication authentication
    ) {
        NoteListResponse.NoteList list = noteService.findNotesByUserUuid(
                uuid,
                request.page(),
                request.size(),
                request.sort(),
                request.isAsc(),
                request.searchBy(),
                request.keyword()
        );
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{uuid}")
    public ResponseEntity<Void> updateNote(
            @PathVariable UUID uuid,
            @Valid @RequestBody NoteUpdateRequest request,
            Authentication authentication
    ) {
        String username = authentication.getName();
        noteService.updateNoteByUuid(uuid, username, request);
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

