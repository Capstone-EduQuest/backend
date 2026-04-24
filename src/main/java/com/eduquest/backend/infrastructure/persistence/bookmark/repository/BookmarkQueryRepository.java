package com.eduquest.backend.infrastructure.persistence.bookmark.repository;

import com.eduquest.backend.infrastructure.persistence.bookmark.entity.BookmarkEntity;
import com.eduquest.backend.infrastructure.persistence.bookmark.repository.impl.BookmarkQRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkQueryRepository extends BookmarkQRepository, JpaRepository<BookmarkEntity, Long> {

}

