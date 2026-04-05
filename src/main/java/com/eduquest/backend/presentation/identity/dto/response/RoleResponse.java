package com.eduquest.backend.presentation.identity.dto.response;

import java.util.UUID;

public record RoleResponse(
        UUID uuid,
        String name
) {

    public static RoleResponse of(UUID uuid, String name) {
        return new RoleResponse(uuid, name);
    }

}
