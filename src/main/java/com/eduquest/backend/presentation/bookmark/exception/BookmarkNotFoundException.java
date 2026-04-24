package com.eduquest.backend.presentation.bookmark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookmarkNotFoundException extends RuntimeException {
    public BookmarkNotFoundException(UUID problemUuid, String username) {
        super(String.format("Bookmark not found for problemUuid=%s, username=%s", problemUuid, username));
    }

    public BookmarkNotFoundException(String message) {
        super(message);
    }
}

