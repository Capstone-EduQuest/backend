package com.eduquest.backend.infrastructure.persistence.bookmark.mapper;

import com.eduquest.backend.domain.bookmark.dto.BookmarkDto;
import com.eduquest.backend.domain.bookmark.model.Bookmark;
import com.eduquest.backend.infrastructure.persistence.bookmark.entity.BookmarkEntity;
import org.springframework.stereotype.Component;

@Component
public class BookmarkEntityMapper {

    public BookmarkEntity toEntity(Long problemId, Long userId) {
        return BookmarkEntity.of(problemId, userId);
    }

    public BookmarkEntity toEntity(Bookmark bookmark) {
        if (bookmark == null) return null;
        return BookmarkEntity.of(bookmark.problemId(), bookmark.userId());
    }

    public BookmarkDto toDto(BookmarkEntity entity) {
        if (entity == null) return null;
        return BookmarkDto.of(entity.getId(), entity.getUuid(), entity.getCreatedAt(), entity.getProblemId(), entity.getUserId());
    }

    public Bookmark toDomain(BookmarkEntity entity) {
        if (entity == null) return null;
        return Bookmark.of(entity.getId(), entity.getUuid(), entity.getCreatedAt(), entity.getProblemId(), entity.getUserId());
    }
}

