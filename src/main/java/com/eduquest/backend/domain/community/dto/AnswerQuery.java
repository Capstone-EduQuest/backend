package com.eduquest.backend.domain.community.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerQuery {

    public record Summary(
            UUID uuid,
            String content,
            UUID userUuid,
            String userNickname,
            Boolean isAdopt,
            LocalDateTime createdAt
    ) {
        public static Summary of(UUID uuid, String content, UUID userUuid, String userNickname, Boolean isAdopt, LocalDateTime createdAt) {
            return new Summary(uuid, content, userUuid, userNickname, isAdopt, createdAt);
        }
    }


}
