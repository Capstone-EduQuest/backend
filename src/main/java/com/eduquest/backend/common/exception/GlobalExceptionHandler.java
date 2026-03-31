package com.eduquest.backend.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EduQuestException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(EduQuestException ex) {
        log.error("커스텀 예외 발생: code={}, message={}", ex.getErrorCode(), ex.getMessage(), ex);
        HttpStatus httpStatus = ex.getErrorCode().getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), ex.getErrorCode().name(), ex.getDetails());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 예상치 못한 오류가 발생했습니다.", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
