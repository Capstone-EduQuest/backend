package com.eduquest.backend.application.identity.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileCommand {

    public record UserListRequest(
            int page,
            int size,
            String sort,
            Boolean isAsc
    ) {

        public static UserListRequest of(int page, int size, String sort, Boolean isAsc) {
            return new UserListRequest(page, size, sort, isAsc);
        }

    }

    @Builder(access = AccessLevel.PROTECTED)
    public record ProfileResponse(
            UUID uuid,
            String userId,
            LocalDate birth,
            String nickname,
            Long point,
            String role,
            Boolean isLocked,
            String profile
    ) {

        public static ProfileResponse of(UUID uuid, String userId, LocalDate birth, String nickname, Long point, String role, Boolean isLocked, String profile) {
            return ProfileResponse.builder()
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

    public record ListResponse(
            int page,
            int size,
            String sort,
            Boolean isAsc,
            List<resultResponse> results
    ) {

        public static ListResponse of(int page, int size, String sort, Boolean isAsc, List<resultResponse> results) {
            return new ListResponse(page, size, sort, isAsc, results);
        }

    }

    public record resultResponse(
            UUID uuid,
            String userId,
            String email,
            String nickname
    ) {

        public static resultResponse of(UUID uuid, String id, String email, String nickname) {
            return new resultResponse(uuid, id, email, nickname);
        }

    }

}
