package com.eduquest.backend.domain.identity.event;

public record FindIdMailEvent(
    String email
) {

    public static FindIdMailEvent of(String email) {
        return new FindIdMailEvent(email);
    }

}
