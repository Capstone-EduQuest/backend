package com.eduquest.backend.domain.learning.event;

import java.util.UUID;

public record StageClearedEvent(UUID userUuid, UUID stageUuid) {

    public static StageClearedEvent of(UUID userUuid, UUID stageUuid) {
        return new StageClearedEvent(userUuid, stageUuid);
    }
}

