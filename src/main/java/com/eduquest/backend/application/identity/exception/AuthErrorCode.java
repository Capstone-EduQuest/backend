package com.eduquest.backend.application.identity.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    INVALID_PASSWORD_RESET_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀번호 재설정 토큰입니다."),
    INVALID_EMAIL_VERIFICATION_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 인증 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
