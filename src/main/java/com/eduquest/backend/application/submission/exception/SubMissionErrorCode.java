package com.eduquest.backend.application.submission.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SubMissionErrorCode implements ErrorCode {

    FORBIDDEN_SUBMISSION_ACCESS(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    EVALUATION_PENDING(HttpStatus.ACCEPTED, "평가가 진행 중입니다."),
    EVALUATION_FAILED(HttpStatus.NO_CONTENT, "평가가 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
