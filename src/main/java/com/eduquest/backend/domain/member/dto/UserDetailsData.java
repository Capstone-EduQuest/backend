package com.eduquest.backend.domain.member.dto;

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
