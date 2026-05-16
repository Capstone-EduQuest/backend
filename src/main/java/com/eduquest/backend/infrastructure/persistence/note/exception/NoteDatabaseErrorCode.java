package com.eduquest.backend.infrastructure.persistence.note.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NoteDatabaseErrorCode implements ErrorCode {

    NOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "노트를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
