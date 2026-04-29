package com.eduquest.backend.presentation.progress.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProgressApiErrorCode implements ErrorCode {

    PROGRESS_NOT_ALLOWED(HttpStatus.FORBIDDEN, "진행 정보를 조회할 권한이 없습니다."),
    PROGRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "진행 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}

