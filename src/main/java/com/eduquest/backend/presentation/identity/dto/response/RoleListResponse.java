package com.eduquest.backend.presentation.identity.dto.response;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class RoleListResponse {

    public record RoleList(
            List<Role> results
    ) {
        public static RoleList of(List<Role> results) {
            return new RoleList(results);
        }
    }

    public record Role(
            UUID uuid,
            String name
    ) {
        public static Role of(UUID uuid, String name) {
            return new Role(uuid, name);
        }
    }

}
