package com.eduquest.backend.domain.bookmark.service;

import com.eduquest.backend.domain.bookmark.dto.BookmarkList;

public interface BookmarkQueryService {

    /**
     * List bookmarks for a given user id with paging and sorting.
     *
     * @param userId user id (DB PK)
     * @param page   page index (0-based)
     * @param size   page size
     * @param sort   sort field name
     * @param isAsc  ascending flag
     * @return BookmarkListResult containing results and paging meta
     */
    BookmarkList listBookmarksByUserId(Long userId, int page, int size, String sort, Boolean isAsc);
}

