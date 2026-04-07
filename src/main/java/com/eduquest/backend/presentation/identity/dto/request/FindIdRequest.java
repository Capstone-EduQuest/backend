package com.eduquest.backend.presentation.identity.dto.request;

import jakarta.validation.constraints.Email;

public record FindIdRequest(
        @Email(message = "유효한 이메일 형식이어야 합니다.") String email
) {

    public static FindIdRequest of(String email) {
        return new FindIdRequest(email);
    }

}
