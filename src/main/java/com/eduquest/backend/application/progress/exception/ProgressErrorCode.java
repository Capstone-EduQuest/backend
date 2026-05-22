package com.eduquest.backend.application.progress.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProgressErrorCode implements ErrorCode {

    FORBIDDEN_PROGRESS_ACCESS(HttpStatus.FORBIDDEN, "권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
