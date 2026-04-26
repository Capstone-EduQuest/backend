package com.eduquest.backend.application.submission.service;

import com.eduquest.backend.application.submission.exception.WrongNoteErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.identity.model.Member;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.domain.submission.dto.WrongNoteQuery;
import com.eduquest.backend.domain.submission.service.WrongNoteCommandService;
import com.eduquest.backend.domain.submission.service.WrongNoteQueryService;
import com.eduquest.backend.presentation.submission.dto.response.WrongNoteListResponse;
import com.eduquest.backend.presentation.submission.dto.response.WrongNoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WrongNoteService {

    private final MemberQueryService memberQueryService;
    private final WrongNoteQueryService wrongNoteQueryService;
    private final WrongNoteCommandService wrongNoteCommandService;

    @Transactional(readOnly = true)
    public WrongNoteListResponse.WrongNoteList findWrongNotesByUserUuid(UUID userUuid, int page, int size, String sortBy, boolean isAsc) {
        Long userId = memberQueryService.findMemberIdByUuid(userUuid);

        String sort = sortBy == null ? "updatedAt" : sortBy;
        List<WrongNoteQuery.Detail> details = wrongNoteQueryService.findWrongDetailNotesByUserId(userId, page, size, sort, isAsc);
        long total = wrongNoteQueryService.countWrongNotesByUserId(userId);

        List<WrongNoteResponse> results = details.stream()
                .map(detail -> WrongNoteResponse.of(
                        detail.id(),
                        detail.problemId(),
                        userUuid,
                        detail.wrongAnswer(),
                        detail.aiExplanation(),
                        detail.isReviewed(),
                        detail.updatedAt(), // lastSubmittedAt
                        detail.createdAt(),
                        detail.updatedAt()
                ))
                .collect(Collectors.toList());

        return WrongNoteListResponse.WrongNoteList.of(page, size, sort, isAsc, total, results);
    }

    @Transactional(readOnly = true)
    public WrongNoteResponse findWrongNoteByUuid(UUID wrongNoteUuid) {
        WrongNoteQuery.Detail detail = wrongNoteQueryService.findWrongDetailNoteByUuid(wrongNoteUuid);
        if (detail == null) {
            throw new EduQuestException(WrongNoteErrorCode.NOT_FOUND);
        }

        // memberQueryService를 사용해 userId -> userUuid 변환
        Member member = memberQueryService.findMemberById(detail.userId());
        java.util.UUID userUuid = member.getUuid();

        return WrongNoteResponse.of(
                detail.id(),
                detail.problemId(),
                userUuid,
                detail.wrongAnswer(),
                detail.aiExplanation(),
                detail.isReviewed(),
                detail.updatedAt(),
                detail.createdAt(),
                detail.updatedAt()
        );
    }

    @Transactional(readOnly = true)
    public WrongNoteListResponse.WrongNoteList findWrongNotes(int page, int size, String sortBy, boolean isAsc) {
        String sort = sortBy == null ? "updatedAt" : sortBy;
        List<WrongNoteQuery.Detail> details = wrongNoteQueryService.findWrongNotes(page, size, sort, isAsc);
        long total = wrongNoteQueryService.countWrongNotes();

        List<WrongNoteResponse> results = details.stream()
                .map(detail -> {
                    com.eduquest.backend.domain.identity.model.Member member = memberQueryService.findMemberById(detail.userId());
                    java.util.UUID userUuid = member.getUuid();
                    return WrongNoteResponse.of(
                            detail.id(),
                            detail.problemId(),
                            userUuid,
                            detail.wrongAnswer(),
                            detail.aiExplanation(),
                            detail.isReviewed(),
                            detail.updatedAt(),
                            detail.createdAt(),
                            detail.updatedAt()
                    );
                })
                .collect(Collectors.toList());

        return WrongNoteListResponse.WrongNoteList.of(page, size, sort, isAsc, total, results);
    }

    @Transactional
    public void deleteWrongNoteByUuid(UUID wrongNoteUuid) {
        wrongNoteCommandService.deleteByUuid(wrongNoteUuid);
    }

}

