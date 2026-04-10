package com.eduquest.backend.presentation.learning.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StageUpdateRequest(
        @NotBlank(message = "스테이지 제목은 필수 입니다.") String title,
        @NotNull(message = "스테이지 번호는 필수 입니다.") Integer number,
        @NotNull(message = "스테이지 리워드는 필수 입니다.") Long reward
) {

}

