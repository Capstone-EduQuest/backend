package com.eduquest.backend.presentation.identity.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserListResponse {

    public record UserList(
            int page,
            int size,
            String sort,
            @JsonProperty("is_asc")
            Boolean isAsc,
            List<result> results
    ) {

        public static UserList of(int page, int size, String sort, Boolean isAsc, List<result> results) {
            return new UserList(page, size, sort, isAsc, results);
        }

    }

    public record result(
            UUID uuid,
            String id,
            String email,
            String nickname
    ) {

        public static result of(UUID uuid, String id, String email, String nickname) {
            return new result(uuid, id, email, nickname);
        }

    }

}
