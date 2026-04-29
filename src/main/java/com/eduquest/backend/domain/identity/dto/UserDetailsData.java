package com.eduquest.backend.domain.identity.dto;

public record UserDetailsData(
        String id,
        String password,
        String role,
        boolean isLocked
) {

    public static UserDetailsData of(String id, String password, String role, boolean isLocked) {
        return new UserDetailsData(id, password, role, isLocked);
    }

}
