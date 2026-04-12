package com.eduquest.backend.presentation.submission.controller;

import com.eduquest.backend.application.submission.service.WrongNoteService;
import com.eduquest.backend.presentation.submission.dto.request.WrongNoteCreateRequest;
import com.eduquest.backend.presentation.submission.dto.request.WrongNoteListRequest;
import com.eduquest.backend.presentation.submission.dto.request.WrongNoteReviewRequest;
import com.eduquest.backend.presentation.submission.dto.response.WrongNoteListResponse;
import com.eduquest.backend.presentation.submission.dto.response.WrongNoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{uuid}/wrongnotes")
public class WrongNoteController {

    private final WrongNoteService wrongNoteService;

    @PreAuthorize("@authz.isSelfByUuid(authentication, #uuid) or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<WrongNoteListResponse.WrongNoteList> listWrongNotes(
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

    @PreAuthorize("@authz.isSelfByUuid(authentication, #uuid) or hasRole('ADMIN')")
    @GetMapping("/{problemId}")
    public ResponseEntity<WrongNoteResponse> getWrongNote(
            @PathVariable UUID uuid,
            @PathVariable Long problemId
    ) {

        WrongNoteResponse response = wrongNoteService.findWrongNoteByUserUuidAndProblemId(uuid, problemId);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@authz.isSelfByUuid(authentication, #uuid) or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createOrUpdateWrongNote(
            @PathVariable UUID uuid,
            @Valid @RequestBody WrongNoteCreateRequest request
    ) {

        Long id = wrongNoteService.createOrUpdateWrongNote(
                uuid,
                request.problemId(),
                request.wrongAnswer(),
                request.feedback()
        );

        URI location = URI.create(String.format("/api/v1/users/%s/wrongnotes/%d", uuid, request.problemId()));
        return ResponseEntity.created(location).body("오답노트 생성/업데이트 성공. id: " + id);
    }

    @PreAuthorize("@authz.isSelfByUuid(authentication, #uuid) or hasRole('ADMIN')")
    @PutMapping("/{problemId}/review")
    public ResponseEntity<Void> reviewWrongNote(
            @PathVariable UUID uuid,
            @PathVariable Long problemId,
            @Valid @RequestBody WrongNoteReviewRequest request
    ) {

        wrongNoteService.markReviewed(uuid, problemId, request.isReviewed());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@authz.isSelfByUuid(authentication, #uuid) or hasRole('ADMIN')")
    @DeleteMapping("/{problemId}")
    public ResponseEntity<Void> deleteWrongNote(
            @PathVariable UUID uuid,
            @PathVariable Long problemId
    ) {

        wrongNoteService.deleteWrongNote(uuid, problemId);

        return ResponseEntity.noContent().build();
    }

}

