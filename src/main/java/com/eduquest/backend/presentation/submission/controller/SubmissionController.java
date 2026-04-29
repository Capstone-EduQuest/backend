package com.eduquest.backend.presentation.submission.controller;

import com.eduquest.backend.application.submission.service.SubmissionService;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.presentation.submission.dto.request.SubmissionRequest;
import com.eduquest.backend.presentation.submission.dto.response.SubmissionResponse;
import com.eduquest.backend.presentation.submission.exception.SubmissionApiErrorCode;
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
    public ResponseEntity<SubmissionResponse> submitProblem(
            @PathVariable UUID problemUuid,
            @Valid @RequestBody SubmissionRequest request
            , Authentication authentication
    ) {

        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new EduQuestException(SubmissionApiErrorCode.SUBMISSION_NOT_ALLOWED);
        }

        String userId = authentication.getName();

        boolean result = submissionService.submit(problemUuid, userId, request.answer());

        return ResponseEntity.status(201).body(SubmissionResponse.ok(result));
    }

}

