package com.eduquest.backend.presentation.community.controller;

import com.eduquest.backend.application.community.dto.CreateQuestionCommand;
import com.eduquest.backend.application.community.dto.QuestionDetailResponse;
import com.eduquest.backend.application.community.dto.QuestionListQuery;
import com.eduquest.backend.application.community.dto.QuestionListResult;
import com.eduquest.backend.application.community.service.QuestionService;
import com.eduquest.backend.presentation.community.dto.request.CreateQuestionRequest;
import com.eduquest.backend.presentation.community.dto.request.QuestionListRequest;
import com.eduquest.backend.presentation.community.dto.response.QuestionListResponse;
import com.eduquest.backend.presentation.community.dto.response.QuestionResponse;
import com.eduquest.backend.presentation.community.dto.response.QuestionSummary;
import com.eduquest.backend.presentation.community.dto.response.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class QuestionController {

    private final QuestionService questionService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/questions")
    public ResponseEntity<Void> createQuestion(@Valid @RequestBody CreateQuestionRequest request, Authentication authentication) {

        questionService.createQuestion(
                CreateQuestionCommand.of(
                        request.title(),
                        request.content(),
                        authentication == null ? null : authentication.getName()
                )
        );

        return ResponseEntity.status(201).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/questions/{questionUuid}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable UUID questionUuid) {
        questionService.deleteQuestionByUuid(questionUuid);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/questions")
    public ResponseEntity<QuestionListResponse> listQuestions(@Valid @ModelAttribute QuestionListRequest request) {

        QuestionListResult result = questionService.findQuestions(
                QuestionListQuery.of(
                        request.page(),
                        request.size(),
                        request.sort(),
                        request.isAsc()
                )
        );

        List<QuestionSummary> summaryList = result.results().stream().map(item -> QuestionSummary.of(
                item.uuid(),
                item.title(),
                UserInfo.of(item.userUuid(), item.userNickname()),
                item.createdAt()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(
                QuestionListResponse.of(
                        result.page(),
                        result.size(),
                        result.sort(),
                        result.isAsc(),
                        summaryList
                )
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/questions/{questionUuid}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable UUID questionUuid) {

        QuestionDetailResponse detailResponse = questionService.findQuestionByUuid(questionUuid);

        return ResponseEntity.ok(
                QuestionResponse.of(
                        detailResponse.uuid(),
                        detailResponse.title(),
                        UserInfo.of(detailResponse.userUuid(), detailResponse.userNickname()),
                        detailResponse.createdAt(),
                        detailResponse.content(),
                        detailResponse.isAdopt(),
                        detailResponse.adoptedAnswerUuid()
                )
        );
    }

}

