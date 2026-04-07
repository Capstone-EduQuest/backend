package com.eduquest.backend.presentation.identity.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder(access = lombok.AccessLevel.PROTECTED)
public record UserProfileResponse(
        UUID uuid,
        @JsonProperty("id")
        String userId,
        LocalDate birth,
        String nickname,
        Long point,
        String role,
        @JsonProperty("is_locked")
        Boolean isLocked,
        String profile
) {

    public static UserProfileResponse of(UUID uuid, String userId, LocalDate birth, String nickname, Long point, String role, Boolean isLocked, String profile) {
        return UserProfileResponse.builder()
                .uuid(uuid)
                .userId(userId)
                .birth(birth)
                .nickname(nickname)
                .point(point)
                .role(role)
                .isLocked(isLocked)
                .profile(profile)
                .build();
    }

}
