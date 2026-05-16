package com.eduquest.backend.infrastructure.persistence.identity.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum IdentityDatabaseErrorCode implements ErrorCode {

    ALREADY_EXIST_MEMBER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "역할을 찾을 수 없습니다."),
    USER_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 역할을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
