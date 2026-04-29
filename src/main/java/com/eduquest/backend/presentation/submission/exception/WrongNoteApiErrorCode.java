package com.eduquest.backend.presentation.submission.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WrongNoteApiErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "오답 노트를 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 오답노트입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}

