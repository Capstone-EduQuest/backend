package com.eduquest.backend.domain.identity.event;

public record SignUpMailEvent(String email) {

    public static SignUpMailEvent of(String email) {
        return new SignUpMailEvent(email);
    }

}

