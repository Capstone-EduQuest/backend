package com.eduquest.backend.infrastructure.persistence.submission.exception;

import com.eduquest.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SubmissionDatabaseErrorCode implements ErrorCode {

    SUBMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "Submission not found"),
    SUBMISSION_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "Submission status not found"),
    WRONG_NOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "Wrong note not found");

    private final HttpStatus httpStatus;
    private final String message;
}
