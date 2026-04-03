package com.eduquest.backend.domain.member.event;

public record ResetPasswordMailEvent(
        String userId,
        String email
) {
}
