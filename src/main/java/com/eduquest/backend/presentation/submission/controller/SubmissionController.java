package com.eduquest.backend.presentation.submission.controller;

import com.eduquest.backend.application.submission.service.SubmissionService;
import com.eduquest.backend.presentation.submission.dto.request.SubmissionRequest;
import com.eduquest.backend.presentation.submission.dto.response.ValuationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SubmissionController {

    private final SubmissionService submissionService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/problems/{problemUuid}/submissions")
    public ResponseEntity<ValuationResponse> submitProblem(
            @PathVariable UUID problemUuid,
            @Valid @RequestBody SubmissionRequest request
            , Authentication authentication
    ) {

        String userId = authentication.getName();

        UUID submissionUuid = submissionService.submit(problemUuid, userId, request.answer());

        ValuationResponse response = ValuationResponse.of(submissionUuid);
        return ResponseEntity.ok(response);

    }

}

