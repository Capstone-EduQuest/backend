package com.eduquest.backend.presentation.bookmark.controller;

import com.eduquest.backend.application.bookmark.dto.BookmarkListResult;
import com.eduquest.backend.application.bookmark.service.BookmarkService;
import com.eduquest.backend.presentation.bookmark.dto.request.BookmarkListRequest;
import com.eduquest.backend.presentation.bookmark.dto.response.BookmarkListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/problems/{problemUuid}/bookmark")
    public ResponseEntity<Void> createBookmark(
            @PathVariable UUID problemUuid,
            Authentication authentication
    ) {
        String username = authentication.getName();
        bookmarkService.createBookmark(problemUuid, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/problems/{problemUuid}/bookmark")
    public ResponseEntity<Void> deleteBookmark(
            @PathVariable UUID problemUuid,
            Authentication authentication
    ) {
        String username = authentication.getName();
        bookmarkService.deleteBookmark(problemUuid, username);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@authz.isSelfByUuid(authentication, #userUuid)")
    @GetMapping("/users/{userUuid}/bookmarks")
    public ResponseEntity<BookmarkListResponse> listBookmarksByUser(
            @PathVariable UUID userUuid,
            @Valid @ModelAttribute BookmarkListRequest request,
            Authentication authentication
    ) {
        BookmarkListResult result = bookmarkService.findBookmarksByUserUuid(
                userUuid,
                request.pageValue(),
                request.sizeValue(),
                request.sortValue(),
                request.isAscValue()
        );

        List<BookmarkListResponse.Result> items = result.results()
                .stream()
                .map(resultDto -> BookmarkListResponse.Result.of(
                        resultDto.stage(),
                        resultDto.type(),
                        resultDto.number(),
                        resultDto.problemUuid()
                ))
                .collect(Collectors.toList());

        boolean isAscValue = result.isAsc() == null ? false : result.isAsc();

        BookmarkListResponse response = BookmarkListResponse.of(
                result.page(),
                result.size(),
                result.sort(),
                isAscValue,
                result.total(),
                items
        );

        return ResponseEntity.ok(response);
    }
}

