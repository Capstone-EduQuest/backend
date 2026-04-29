package com.eduquest.backend.application.bookmark.service;

import com.eduquest.backend.application.bookmark.dto.BookmarkListResult;
import com.eduquest.backend.application.bookmark.exception.BookmarkErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.bookmark.dto.BookmarkList;
import com.eduquest.backend.domain.bookmark.service.BookmarkCommandService;
import com.eduquest.backend.domain.bookmark.service.BookmarkQueryService;
import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final MemberQueryService memberQueryService;
    private final ProblemQueryService problemQueryService;
    private final BookmarkCommandService bookmarkCommandService;
    private final BookmarkQueryService bookmarkQueryService;

    @Transactional
    public void createBookmark(UUID problemUuid, String username) {

        if (username == null || username.isBlank() || problemUuid == null) {
            throw new EduQuestException(BookmarkErrorCode.INVALID_REQUEST);
        }

        Long memberId = memberQueryService.findMemberIdByUserId(username);

        ProblemQuery.Detail detail = problemQueryService.findProblemByUuid(problemUuid);
        if (detail == null) {
            throw new EduQuestException(BookmarkErrorCode.NOT_FOUND);
        }
        Long problemId = detail.id();

        try {
            bookmarkCommandService.createBookmark(memberId, problemId);
        } catch (Exception ex) {
            throw new EduQuestException(BookmarkErrorCode.CONFLICT);
        }

    }

    @Transactional
    public void deleteBookmark(UUID problemUuid, String username) {

        if (username == null || username.isBlank() || problemUuid == null) {
            throw new EduQuestException(BookmarkErrorCode.INVALID_REQUEST);
        }

        Long memberId = memberQueryService.findMemberIdByUserId(username);

        ProblemQuery.Detail detail = problemQueryService.findProblemByUuid(problemUuid);
        if (detail == null) {
            throw new EduQuestException(BookmarkErrorCode.NOT_FOUND);
        }

        Long problemId = detail.id();

        bookmarkCommandService.deleteBookmark(memberId, problemId);

    }

    @Transactional(readOnly = true)
    public BookmarkListResult findBookmarksByUserUuid(UUID userUuid, int page, int size, String sort, Boolean isAsc) {

        if (userUuid == null) {
            throw new EduQuestException(BookmarkErrorCode.INVALID_REQUEST);
        }

        Long memberId = memberQueryService.findMemberIdByUuid(userUuid);

        BookmarkList domainResult = bookmarkQueryService.listBookmarksByUserId(memberId, page, size, sort, isAsc);

        List<BookmarkListResult.Item> items = domainResult.results()
                .stream()
                .map(summary -> BookmarkListResult.Item.of(
                        summary.stage(),
                        summary.type(),
                        summary.number(),
                        summary.problemUuid()
                ))
                .toList();

        return BookmarkListResult.of(domainResult.page(), domainResult.size(), domainResult.sort(), domainResult.isAsc(), domainResult.total(), items);
    }

}

