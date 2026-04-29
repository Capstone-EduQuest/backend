package com.eduquest.backend.presentation.learning.dto.response;

import java.util.UUID;

public record StageResponse(UUID uuid, String title, Integer number, Long reward) {

    public static StageResponse of(UUID uuid, String title, Integer number, Long reward) {
        return new StageResponse(uuid, title, number, reward);
    }

}

