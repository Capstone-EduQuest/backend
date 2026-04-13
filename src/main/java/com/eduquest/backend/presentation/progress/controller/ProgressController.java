package com.eduquest.backend.presentation.progress.controller;

import com.eduquest.backend.application.progress.dto.ProgressDto;
import com.eduquest.backend.application.progress.service.ProgressService;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.presentation.progress.dto.response.ProgressListResponse;
import com.eduquest.backend.presentation.progress.dto.response.ProgressResponse;
import com.eduquest.backend.presentation.progress.exception.ProgressApiErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 사용자 별 진행 정보 조회 컨트롤러
 * GET /api/v1/users/{userUuid}/progress
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/users/{userUuid}/progress")
    public ResponseEntity<ProgressListResponse> getUserProgress(
            @PathVariable UUID userUuid,
            Authentication authentication
    ) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new EduQuestException(ProgressApiErrorCode.PROGRESS_NOT_ALLOWED);
        }

        String requesterUserId = authentication.getName();

        List<ProgressDto.ProgressItem> progressItems = progressService.findByUserUuid(userUuid, requesterUserId);

        List<ProgressResponse> results = progressItems.stream()
                .map(item -> ProgressResponse.of(item.stage(), item.totalQuestionCount(), item.clear()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ProgressListResponse.of(results));
    }

}

