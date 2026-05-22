package com.eduquest.backend.infrastructure.persistence.bookmark.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookMarkDatabaseErrorCode implements ErrorCode {

    USERID_OR_PROBLEM_ID_IS_NULL(HttpStatus.BAD_REQUEST, "유저 id 또는 문제 id가 null입니다."),
    ALREADY_EXIST_BOOKMARK(HttpStatus.CONFLICT, "이미 북마크한 문제입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
