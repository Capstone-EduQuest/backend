package com.eduquest.backend.domain.community.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionQuery {

    public record Summary(
            UUID uuid,
            String title,
            UUID userUuid,
            String userNickname,
            LocalDateTime createdAt
    ) {
        public static Summary of(UUID uuid, String title, UUID userUuid, String userNickname, LocalDateTime createdAt) {
            return new Summary(uuid, title, userUuid, userNickname, createdAt);
        }
    }

    public record Detail(
            UUID uuid,
            String title,
            UUID userUuid,
            String userNickname,
            LocalDateTime createdAt,
            String content,
            boolean isAdopted,
            UUID adoptedAnswerUuid
    ) {

        public static Detail of(
                UUID uuid,
                String title,
                UUID userUuid,
                String userNickname,
                LocalDateTime createdAt,
                String content,
                boolean isAdopted,
                UUID adoptedAnswerUuid
        ) {

            return new Detail(uuid, title, userUuid, userNickname, createdAt, content, isAdopted, adoptedAnswerUuid);

        }

    }

}

