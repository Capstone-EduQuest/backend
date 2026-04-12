package com.eduquest.backend.presentation.submission.controller;

import com.eduquest.backend.application.submission.service.WrongNoteService;
import com.eduquest.backend.presentation.submission.dto.request.WrongNoteListRequest;
import com.eduquest.backend.presentation.submission.dto.response.WrongNoteListResponse;
import com.eduquest.backend.presentation.submission.dto.response.WrongNoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wrong-notes")
public class WrongNoteController {

    private final WrongNoteService wrongNoteService;

    // GET /api/v1/wrong-notes/{uuid}
    @GetMapping("/{uuid}")
    public ResponseEntity<WrongNoteResponse> getWrongNote(@PathVariable UUID uuid) {
        WrongNoteResponse dto = wrongNoteService.findWrongNoteByUuid(uuid);
        return ResponseEntity.ok(dto);
    }

    // GET /api/v1/wrong-notes/users/{uuid}
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

    // GET /api/v1/wrong-notes?page=&size=&sort=&is_asc=
    @GetMapping
    public ResponseEntity<WrongNoteListResponse.WrongNoteList> listAll(
            @Valid @ModelAttribute WrongNoteListRequest request
    ) {
        WrongNoteListResponse.WrongNoteList list = wrongNoteService.findWrongNotes(
                request.page(), request.size(), request.sort(), request.isAsc()
        );
        return ResponseEntity.ok(list);
    }

    // DELETE /api/v1/wrong-notes/{uuid} (관리자만 가능)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteByUuid(@PathVariable UUID uuid) {
        wrongNoteService.deleteWrongNoteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

}

