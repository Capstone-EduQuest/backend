package com.eduquest.backend.domain.bookmark.service;

public interface BookmarkCommandService {

    /**
     * Create a bookmark for (userId, problemId).
     *
     * @param userId    user id (DB PK)
     * @param problemId problem id (DB PK)
     */
    void createBookmark(Long userId, Long problemId);

    /**
     * Delete bookmark for (userId, problemId).
     *
     * @param userId    user id
     * @param problemId problem id
     */
    void deleteBookmark(Long userId, Long problemId);

}

