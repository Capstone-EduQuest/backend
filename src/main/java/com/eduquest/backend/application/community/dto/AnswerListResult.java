package com.eduquest.backend.application.community.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AnswerListResult(
        int page,
        int size,
        String sort,
        Boolean isAsc,
        List<Item> results
) {
    public static AnswerListResult of(int page, int size, String sort, Boolean isAsc, List<Item> results) {
        return new AnswerListResult(page, size, sort, isAsc, results);
    }

    public record Item(
            UUID uuid,
            String content,
            UUID userUuid,
            String userNickname,
            Boolean isAdopt,
            LocalDateTime createdAt
    ) {
        public static Item of(UUID uuid, String content, UUID userUuid, String userNickname, Boolean isAdopt, LocalDateTime createdAt) {
            return new Item(uuid, content, userUuid, userNickname, isAdopt, createdAt);
        }
    }
}

