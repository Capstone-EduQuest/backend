package com.eduquest.backend.application.learning.dto;

import java.util.UUID;

public record StageDto(
        UUID uuid,
        String title,
        Integer number,
        Long reward
) {

    public static StageDto of(UUID uuid, String title, Integer number, Long reward) {
        return new StageDto(uuid, title, number, reward);
    }

}

