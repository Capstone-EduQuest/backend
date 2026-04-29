package com.eduquest.backend.infrastructure.s3.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {

    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드에 실패했습니다."),
    S3_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 삭제에 실패했습니다."),
    S3_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "S3에서 파일을 찾을 수 없습니다."),
    S3_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 연결에 실패했습니다."),
    S3_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "S3 권한이 없습니다."),
    S3_INVALID_BUCKET(HttpStatus.BAD_REQUEST, "S3 버킷이 유효하지 않습니다."),
    S3_FILE_TOO_LARGE(HttpStatus.BAD_REQUEST, "S3 파일이 너무 큽니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
