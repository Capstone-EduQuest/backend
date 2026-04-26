package com.eduquest.backend.presentation.submission.controller;

import com.eduquest.backend.application.submission.service.WrongNoteService;
import com.eduquest.backend.presentation.submission.dto.request.WrongNoteListRequest;
import com.eduquest.backend.presentation.submission.dto.response.WrongNoteListResponse;
import com.eduquest.backend.presentation.submission.dto.response.WrongNoteResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wrong-notes")
public class WrongNoteController {

    private final WrongNoteService wrongNoteService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{uuid}")
    public ResponseEntity<WrongNoteResponse> getWrongNote(@PathVariable UUID uuid) {
        WrongNoteResponse dto = wrongNoteService.findWrongNoteByUuid(uuid);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("@authz.isSelfByUuid(authentication, #uuid) or hasRole('ADMIN')")
    @GetMapping("/users/{uuid}")
    public ResponseEntity<WrongNoteListResponse.WrongNoteList> listByUser(
            @PathVariable UUID uuid,
            @Valid @ModelAttribute WrongNoteListRequest request
    ) {
        WrongNoteListResponse.WrongNoteList list = wrongNoteService.findWrongNotesByUserUuid(
                uuid,
                request.page(),
                request.size(),
                request.sort(),
                request.isAsc()
        );

        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<WrongNoteListResponse.WrongNoteList> listAll(
            @Valid @ModelAttribute WrongNoteListRequest request
    ) {
        WrongNoteListResponse.WrongNoteList list = wrongNoteService.findWrongNotes(
                request.page(), request.size(), request.sort(), request.isAsc()
        );
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteByUuid(@PathVariable UUID uuid) {
        wrongNoteService.deleteWrongNoteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{uuid}/ai-feedback")
    public ResponseEntity<Void> putWrongNoteAiExplain(@PathVariable @NotNull UUID uuid, Authentication authentication) {
        wrongNoteService.requestAiFeedback(uuid, authentication.getName());
        return ResponseEntity.noContent().build();
    }

}

