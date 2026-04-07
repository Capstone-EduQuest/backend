package com.eduquest.backend.presentation.identity.dto.response;

import java.util.UUID;

public record UserIdToUuidResponse(
        UUID uuid
) {

    public static UserIdToUuidResponse of(UUID uuid) {
        return new UserIdToUuidResponse(uuid);
    }

}
