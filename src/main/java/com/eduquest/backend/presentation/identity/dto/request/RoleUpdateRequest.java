package com.eduquest.backend.presentation.identity.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RoleUpdateRequest(
        @NotNull(message = "role은 필수입니다.")
        @JsonProperty("role_uuid")
        UUID uuid
) {
    public static RoleUpdateRequest of(UUID uuid) {
        return new RoleUpdateRequest(uuid);
    }
}
