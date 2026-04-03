package com.eduquest.backend.presentation.identity.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        String id,
        String password
) {

    public static SignInRequest of(
            @NotBlank(message = "아이디는 필수 입니다.")
            String id,
            @NotBlank(message = "비밀번호는 필수 입니다.")
            String password
    ) {
        return new SignInRequest(id, password);
    }

}
