package com.eduquest.backend.infrastructure.security.dto;

public record SignInData(
        String id,
        String password
) {

    public static SignInData of(String id, String password) {
        return new SignInData(id, password);
    }

}
