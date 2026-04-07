package com.eduquest.backend.presentation.identity.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FindPasswordRequest(
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        String email,
        @JsonProperty("id")
        @NotBlank(message = "아이디는 필수 입력값입니다.")
        String userId
) {

    public static FindPasswordRequest of(String email, String userId) {
        return new FindPasswordRequest(email, userId);
    }

}
