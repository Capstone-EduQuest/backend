package com.eduquest.backend.presentation.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(
        @NotBlank(message = "email은 필수입니다.")
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        String email,
        @NotBlank(message = "password는 필수입니다.")
        @Size(min = 8, max = 64, message = "8자 이상 64자 이하 이어야 합니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\W_]).*$",
                message = "영문, 숫자, 특수문자를 포함해야 합니다."
        )
        String password,
        @NotBlank(message = "nickname은 필수입니다.")
        String nickname
) {
    public static ProfileUpdateRequest of(String email, String password, String nickname) {
        return new ProfileUpdateRequest(email, password, nickname);
    }
}
