package com.eduquest.backend.domain.member.event;

public record ResetPasswordMailEvent(
        String userId,
        String email
) {

    public static ResetPasswordMailEvent of(String userId, String email) {
        return new ResetPasswordMailEvent(userId, email);
    }

}
