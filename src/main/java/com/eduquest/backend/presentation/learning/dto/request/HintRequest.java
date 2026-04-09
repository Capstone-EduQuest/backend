package com.eduquest.backend.presentation.learning.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HintRequest(
        @NotNull(message = "힌트 레벨은 필수 입니다.") Integer level,
        @NotNull(message = "힌트 포인트는 필수 입니다.") Long point,
        @NotBlank(message = "힌트 내용은 필수 입니다.") String content
) {

    public static HintRequest of(Integer level, Long point, String content) {
        return new HintRequest(level, point, content);
    }

}

