package com.eduquest.backend.infrastructure.persistence.learning.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LearningDatabaseErrorCode implements ErrorCode {

    ALREADY_EXISTS_STAGE(HttpStatus.BAD_REQUEST, "이미 존재하는 스테이지입니다."),
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "문제를 찾을 수 없습니다."),
    HINT_NOT_FOUND(HttpStatus.NOT_FOUND, "힌트를 찾을 수 없습니다."),
    STAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "스테이지를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
