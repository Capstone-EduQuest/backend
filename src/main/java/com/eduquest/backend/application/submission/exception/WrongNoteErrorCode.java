package com.eduquest.backend.application.submission.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * application 계층에서 사용하는 오답노트(WrongNote) 관련 에러 코드.
 * presentation 계층의 ApiErrorCode와 책임을 분리하기 위해 application에 별도 정의합니다.
 */
@Getter
@RequiredArgsConstructor
public enum WrongNoteErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "오답 노트를 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 오답노트입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

