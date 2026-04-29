package com.eduquest.backend.presentation.community.dto.response;

import java.util.UUID;

public record UserInfo(
        UUID uuid,
        String nickname
) {
    public static UserInfo of(UUID uuid, String nickname) {
        return new UserInfo(uuid, nickname);
    }
}

