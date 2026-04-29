package com.eduquest.backend.common.exception;

import java.util.Map;

public record ErrorResponse(
        int code,
        String message,
        Map<String, Object> details
) {
}
