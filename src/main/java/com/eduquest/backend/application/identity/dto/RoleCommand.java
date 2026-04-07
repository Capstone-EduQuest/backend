package com.eduquest.backend.application.identity.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleCommand {

    public record RoleResponse(
            UUID uuid,
            String name
    ) {
        public static RoleResponse of(UUID uuid, String name) {
            return new RoleResponse(uuid, name);
        }
    }

    public record RoleUpdateRequest(
            UUID userUuid,
            UUID roleUuid
    ) {
        public static RoleUpdateRequest of(UUID userUuid, UUID roleUuid) {
            return new RoleUpdateRequest(userUuid, roleUuid);
        }
    }

}
