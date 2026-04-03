package com.eduquest.backend.domain.member.event;

public record FindIdMailEvent(
    String email
) {

    public static FindIdMailEvent of(String email) {
        return new FindIdMailEvent(email);
    }

}
