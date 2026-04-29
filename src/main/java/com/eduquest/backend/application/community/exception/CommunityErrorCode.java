package com.eduquest.backend.application.community.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommunityErrorCode implements ErrorCode {

    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 질문을 찾을 수 없습니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 답변을 찾을 수 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}

