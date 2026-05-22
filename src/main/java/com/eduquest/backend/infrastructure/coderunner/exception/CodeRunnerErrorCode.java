package com.eduquest.backend.infrastructure.coderunner.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CodeRunnerErrorCode implements ErrorCode {

    CODE_RUNNER_SERVER_ERROR(HttpStatus.BAD_REQUEST, "CodeRunnerServerError"),
    CODE_RUNNER_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CodeRunnerClientError");

    private final HttpStatus httpStatus;
    private final String message;
}
