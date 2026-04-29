package com.eduquest.backend.infrastructure.persistence.bookmark.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.bookmark.service.BookmarkCommandService;
import com.eduquest.backend.infrastructure.persistence.bookmark.mapper.BookmarkEntityMapper;
import com.eduquest.backend.infrastructure.persistence.bookmark.repository.BookmarkJpaRepository;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JpaBookmarkCommandService implements BookmarkCommandService {

    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final BookmarkEntityMapper mapper;

    @Override
    @Transactional
    public void createBookmark(Long userId, Long problemId) {
        if (userId == null || problemId == null) {
            throw new IllegalArgumentException("userId and problemId must not be null");
        }

        boolean exists = bookmarkJpaRepository.findByUserIdAndProblemId(userId, problemId).isPresent();
        if (exists) {
            throw new EduQuestException(DataBaseErrorCode.ALREADY_EXIST_MEMBER);
        }

        bookmarkJpaRepository.save(mapper.toEntity(problemId, userId));
    }

    @Override
    @Transactional
    public void deleteBookmark(Long userId, Long problemId) {
        if (userId == null || problemId == null) {
            throw new IllegalArgumentException("userId and problemId must not be null");
        }
        bookmarkJpaRepository.deleteByUserIdAndProblemId(userId, problemId);
    }
}

