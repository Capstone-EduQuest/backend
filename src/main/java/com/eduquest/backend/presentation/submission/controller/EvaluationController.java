package com.eduquest.backend.presentation.submission.controller;

import com.eduquest.backend.application.submission.service.EvaluationService;
import com.eduquest.backend.application.submission.dto.EvaluationInfo;
import com.eduquest.backend.presentation.submission.dto.response.EvaluationPollingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/problems/evaluations/{submissionUuid}")
    public ResponseEntity<EvaluationPollingResponse> findEvaluation(
            @PathVariable UUID submissionUuid,
            Authentication authentication) {

        String userId = authentication.getName();
        EvaluationInfo info = evaluationService.findBySubmissionUuid(submissionUuid, userId);
        if (info == null) {
            return ResponseEntity.ok(EvaluationPollingResponse.pending());
        }

        return ResponseEntity.ok(EvaluationPollingResponse.completed(info.isCorrect()));
    }
}

