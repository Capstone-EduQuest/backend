package com.eduquest.backend.domain.bookmark.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkQuery {

    public record Summary(
            String stage,
            String type,
            Integer number,
            UUID problemUuid
    ) {
        public static Summary of(String stage, String type, Integer number, UUID problemUuid) {
            return new Summary(stage, type, number, problemUuid);
        }
    }

}

