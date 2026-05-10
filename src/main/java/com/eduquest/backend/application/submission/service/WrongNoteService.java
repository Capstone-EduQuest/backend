package com.eduquest.backend.application.submission.service;

import com.eduquest.backend.application.submission.dto.WrongNoteDto;
import com.eduquest.backend.application.submission.dto.WrongNoteListDto;
import com.eduquest.backend.application.submission.exception.WrongNoteErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.identity.model.Member;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.domain.learning.model.Problem;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.domain.submission.dto.WrongNoteQuery;
import com.eduquest.backend.domain.submission.event.WrongNoteAiFeedBackEvent;
import com.eduquest.backend.domain.submission.service.WrongNoteCommandService;
import com.eduquest.backend.domain.submission.service.WrongNoteQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WrongNoteService {

    private final MemberQueryService memberQueryService;
    private final WrongNoteQueryService wrongNoteQueryService;
    private final WrongNoteCommandService wrongNoteCommandService;
    private final ProblemQueryService problemQueryService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public WrongNoteListDto findWrongNotesByUserUuid(UUID userUuid, int page, int size, String sortBy, boolean isAsc) {
        Long userId = memberQueryService.findMemberIdByUuid(userUuid);

        String sort = sortBy == null ? "updatedAt" : sortBy;
        List<WrongNoteQuery.Detail> details = wrongNoteQueryService.findWrongDetailNotesByUserId(userId, page, size, sort, isAsc);
        long total = wrongNoteQueryService.countWrongNotesByUserId(userId);

        List<WrongNoteDto> results = details.stream()
                .map(detail -> WrongNoteDto.of(
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

        return WrongNoteListDto.of(page, size, sort, isAsc, total, results);
    }

    @Transactional(readOnly = true)
    public WrongNoteDto findWrongNoteByUuid(UUID wrongNoteUuid, String userId) {
        WrongNoteQuery.Detail detail = wrongNoteQueryService.findWrongDetailNoteByUuid(wrongNoteUuid);
        if (detail == null) {
            throw new EduQuestException(WrongNoteErrorCode.NOT_FOUND);
        }

        // memberQueryService를 사용해 userId -> userUuid 변환
        Member member = memberQueryService.findMemberById(detail.userId());

        if (!userId.equals(member.getUserId())) {
            throw new EduQuestException(WrongNoteErrorCode.FORBIDDEN);
        }

        UUID userUuid = member.getUuid();

        return WrongNoteDto.of(
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
    public WrongNoteListDto findWrongNotes(int page, int size, String sortBy, boolean isAsc) {
        String sort = sortBy == null ? "updatedAt" : sortBy;
        List<WrongNoteQuery.Detail> details = wrongNoteQueryService.findWrongNotes(page, size, sort, isAsc);
        long total = wrongNoteQueryService.countWrongNotes();

        Map<Long, UUID> userUuidMap = memberQueryService.findMemberUuidByUserIds(
                details.stream()
                        .map(WrongNoteQuery.Detail::userId)
                        .toList()
        );

        List<WrongNoteDto> results = details.stream()
                .map(detail -> {
                    return WrongNoteDto.of(
                            detail.id(),
                            detail.problemId(),
                            userUuidMap.get(detail.userId()),
                            detail.wrongAnswer(),
                            detail.aiExplanation(),
                            detail.isReviewed(),
                            detail.updatedAt(),
                            detail.createdAt(),
                            detail.updatedAt()
                    );
                })
                .collect(Collectors.toList());

        return WrongNoteListDto.of(page, size, sort, isAsc, total, results);
    }

    @Transactional
    public void deleteWrongNoteByUuid(UUID wrongNoteUuid) {
        wrongNoteCommandService.deleteByUuid(wrongNoteUuid);
    }

    @Transactional
    public void requestAiFeedback(UUID wrongNoteUuid, String userId) {

        if (!memberQueryService.isExistByUserId(userId)) {
            throw new EduQuestException(WrongNoteErrorCode.NOT_FOUND);
        }

        // wrong note 존재 확인
        Long memberId = memberQueryService.findMemberIdByUserId(userId);
        WrongNoteQuery.Detail detail = wrongNoteQueryService.findWrongDetailNoteByUuid(wrongNoteUuid);
        Problem problem = problemQueryService.findProblemById(detail.problemId());

        if (!detail.userId().equals(memberId)) {
            throw new EduQuestException(WrongNoteErrorCode.FORBIDDEN);
        }

        // 이벤트 발행
        WrongNoteAiFeedBackEvent event = new WrongNoteAiFeedBackEvent(
                wrongNoteUuid,
                problem.getSummary(),
                problem.getExpectedOutput(),
                problem.getBlock(),
                detail.wrongAnswer()
        );

        eventPublisher.publishEvent(event);
    }

}

