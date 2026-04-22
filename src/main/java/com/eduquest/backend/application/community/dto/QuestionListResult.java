package com.eduquest.backend.application.community.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record QuestionListResult(
        int page,
        int size,
        String sort,
        Boolean isAsc,
        List<Item> results
) {
    public static QuestionListResult of(int page, int size, String sort, Boolean isAsc, List<Item> results) {
        return new QuestionListResult(page, size, sort, isAsc, results);
    }

    public record Item(
            UUID uuid,
            String title,
            UUID userUuid,
            String userNickname,
            LocalDateTime createdAt
    ) {
        public static Item of(UUID uuid, String title, UUID userUuid, String userNickname, LocalDateTime createdAt) {
            return new Item(uuid, title, userUuid, userNickname, createdAt);
        }
    }
}

