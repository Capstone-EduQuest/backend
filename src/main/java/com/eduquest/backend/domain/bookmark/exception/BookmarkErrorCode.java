package com.eduquest.backend.domain.bookmark.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookmarkErrorCode implements ErrorCode {

    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크를 찾을 수 없습니다."),
    BOOKMARK_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 북마크가 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;

}

