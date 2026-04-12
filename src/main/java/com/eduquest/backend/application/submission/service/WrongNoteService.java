package com.eduquest.backend.application.submission.service;

import com.eduquest.backend.application.submission.exception.WrongNoteErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import com.eduquest.backend.domain.submission.dto.WrongNoteDto;
import com.eduquest.backend.domain.submission.model.WrongNote;
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
        List<WrongNoteDto.Detail> details = wrongNoteQueryService.findWrongNotesByUserId(userId, page, size, sort, isAsc);
        long total = wrongNoteQueryService.countWrongNotesByUserId(userId);

        List<WrongNoteResponse> results = details.stream()
                .map(d -> WrongNoteResponse.of(
                        d.id(),
                        d.problemId(),
                        userUuid,
                        d.wrongAnswer(),
                        d.aiExplanation(),
                        d.isReviewed(),
                        d.updatedAt(), // lastSubmittedAt
                        d.createdAt(),
                        d.updatedAt()
                ))
                .collect(Collectors.toList());

        return WrongNoteListResponse.WrongNoteList.of(page, size, sort, isAsc, total, results);
    }

    @Transactional(readOnly = true)
    public WrongNoteResponse findWrongNoteByUserUuidAndProblemId(UUID userUuid, Long problemId) {
        Long userId = memberQueryService.findMemberIdByUuid(userUuid);
        WrongNoteDto.Detail detail = wrongNoteQueryService.findWrongNoteByUserIdAndProblemId(userId, problemId);
        if (detail == null) {
            throw new EduQuestException(WrongNoteErrorCode.NOT_FOUND);
        }

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

    @Transactional
    public Long createOrUpdateWrongNote(UUID userUuid, Long problemId, String wrongAnswer, String feedback) {
        Long userId = memberQueryService.findMemberIdByUuid(userUuid);

        WrongNote wn = WrongNote.of(userId, problemId, wrongAnswer, feedback);
        return wrongNoteCommandService.saveOrUpdateWrongNote(wn);
    }

    @Transactional
    public void markReviewed(UUID userUuid, Long problemId, Boolean isReviewed) {
        Long userId = memberQueryService.findMemberIdByUuid(userUuid);
        WrongNoteDto.Detail detail = wrongNoteQueryService.findWrongNoteByUserIdAndProblemId(userId, problemId);
        if (detail == null) {
            throw new EduQuestException(WrongNoteErrorCode.NOT_FOUND);
        }
        wrongNoteCommandService.markReviewed(userId, problemId, Boolean.TRUE.equals(isReviewed));
    }

    @Transactional
    public void deleteWrongNote(UUID userUuid, Long problemId) {
        Long userId = memberQueryService.findMemberIdByUuid(userUuid);
        wrongNoteCommandService.deleteByUserIdAndProblemId(userId, problemId);
    }

}

