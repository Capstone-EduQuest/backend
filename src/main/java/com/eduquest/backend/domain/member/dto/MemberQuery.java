package com.eduquest.backend.domain.member.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberQuery {

    public record EmailAndUserId(String email, String userId) {

        public static EmailAndUserId of(String email, String userId) {
            return new EmailAndUserId(email, userId);
        }

    }

    public record UserProfile(
            UUID uuid,
            String userId,
            LocalDate birth,
            String nickname,
            Long point,
            String role,
            Boolean isLocked,
            Long profileId
    ) {

        public static UserProfile of(
                UUID uuid,
                String userId,
                LocalDate birth,
                String nickname,
                Long point,
                String role,
                Boolean isLocked,
                Long profileId
        ) {
            return new UserProfile(uuid, userId, birth, nickname, point, role, isLocked, profileId);
        }

    }

    public record UserListResult(
            UUID uuid,
            String userId,
            String email,
            String nickname
    ) {

        public static UserListResult of(
                UUID uuid,
                String id,
                String email,
                String nickname
        ) {
            return new UserListResult(uuid, id, email, nickname);
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
