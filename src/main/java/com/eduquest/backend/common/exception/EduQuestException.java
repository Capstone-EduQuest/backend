package com.eduquest.backend.common.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class EduQuestException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public EduQuestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public EduQuestException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
    }

}
