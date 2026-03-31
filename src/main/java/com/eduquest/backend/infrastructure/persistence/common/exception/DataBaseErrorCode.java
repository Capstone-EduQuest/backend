package com.eduquest.backend.infrastructure.persistence.common.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DataBaseErrorCode implements ErrorCode {

    ALREADY_EXIST_MEMBER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),
    NOT_FOUND_DATA(HttpStatus.NOT_FOUND, "데이터를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
