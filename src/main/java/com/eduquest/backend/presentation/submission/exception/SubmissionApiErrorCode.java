package com.eduquest.backend.presentation.submission.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SubmissionApiErrorCode implements ErrorCode {

    SUBMISSION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "제출 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}

