package com.eduquest.backend.infrastructure.persistence.bookmark.service;

import com.eduquest.backend.domain.bookmark.dto.BookmarkList;
import com.eduquest.backend.domain.bookmark.dto.BookmarkQuery;
import com.eduquest.backend.domain.bookmark.service.BookmarkQueryService;
import com.eduquest.backend.infrastructure.persistence.bookmark.repository.BookmarkQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaBookmarkQueryService implements BookmarkQueryService {

    private final BookmarkQueryRepository bookmarkQueryRepository;

    @Override
    public BookmarkList listBookmarksByUserId(Long userId, int page, int size, String sort, Boolean isAsc) {
        boolean asc = isAsc != null && isAsc;
        List<BookmarkQuery.Summary> results = bookmarkQueryRepository.findByUserId(userId, page, size, sort, asc);
        long total = bookmarkQueryRepository.countByUserId(userId);
        return BookmarkList.of(page, size, sort, isAsc, total, results);
    }
}

