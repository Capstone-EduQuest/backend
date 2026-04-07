package com.eduquest.backend.presentation.identity.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        String token,
        @NotBlank(message = "password는 필수입니다.")
        @Size(min = 8, max = 64, message = "8자 이상 64자 이하 이어야 합니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\W_]).*$",
                message = "영문, 숫자, 특수문자를 포함해야 합니다."
        )
        @JsonProperty("new_password")
        String newPassword
) {
}
