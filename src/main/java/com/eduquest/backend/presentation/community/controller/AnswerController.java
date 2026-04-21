package com.eduquest.backend.presentation.community.controller;

import com.eduquest.backend.application.community.dto.AnswerListQuery;
import com.eduquest.backend.application.community.dto.AnswerListResult;
import com.eduquest.backend.application.community.dto.CreateAnswerCommand;
import com.eduquest.backend.application.community.service.AnswerService;
import com.eduquest.backend.presentation.community.dto.request.AnswerListRequest;
import com.eduquest.backend.presentation.community.dto.request.CreateAnswerRequest;
import com.eduquest.backend.presentation.community.dto.response.AnswerListResponse;
import com.eduquest.backend.presentation.community.dto.response.AnswerSummary;
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
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/question/{questionUuid}/answers")
    public ResponseEntity<Void> createAnswer(
            @PathVariable UUID questionUuid,
            @Valid @RequestBody CreateAnswerRequest request,
            Authentication authentication
    ) {

        answerService.createAnswer(
                CreateAnswerCommand.of(
                        questionUuid,
                        request.content(),
                        authentication == null ? null : authentication.getName()
                )
        );

        return ResponseEntity.status(201).build();
    }

    @GetMapping("/questions/{questionUuid}/answers")
    public ResponseEntity<AnswerListResponse> listAnswers(
            @PathVariable UUID questionUuid,
            @Valid @ModelAttribute AnswerListRequest request
    ) {

        AnswerListResult result = answerService.findAnswersByQuestionUuid(
                questionUuid,
                AnswerListQuery.of(
                        request.page(),
                        request.size(),
                        request.isAsc()
                )
        );

        List<AnswerSummary> answerSummaryList = result.results().stream().map(item -> AnswerSummary.of(
                item.uuid(),
                item.content(),
                UserInfo.of(item.userUuid(), item.userNickname()),
                item.isAdopt(),
                item.createdAt()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(
                AnswerListResponse.of(
                        result.page(),
                        result.size(),
                        result.sort(),
                        result.isAsc(),
                        answerSummaryList
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/answers/{answerUuid}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable UUID answerUuid) {
        answerService.deleteAnswerByUuid(answerUuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/answers/{answerUuid}/adopt")
    public ResponseEntity<Void> adoptAnswer(@PathVariable UUID answerUuid, Authentication authentication) {
        answerService.adoptAnswer(answerUuid, authentication == null ? null : authentication.getName());
        return ResponseEntity.status(201).build();
    }

}

