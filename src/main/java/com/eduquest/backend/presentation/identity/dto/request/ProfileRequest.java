package com.eduquest.backend.presentation.identity.dto.request;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.presentation.identity.exception.IdentityApiErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ProfileRequest(
        @NotBlank(message = "id는 필수입니다.")
        String id,
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
        @JsonProperty("password_valid")
        @NotBlank(message = "passwordValid는 필수입니다.")
        String passwordValid,
        @NotNull(message = "birth는 필수입니다.")
        LocalDate birth,
        @NotBlank(message = "nickname은 필수입니다.")
        String nickname
) {

        public ProfileRequest {
                if (!password.equals(passwordValid)) {
                        throw new EduQuestException(IdentityApiErrorCode.PASSWORD_VALID_NOT_SAME);
                }
        }

}
