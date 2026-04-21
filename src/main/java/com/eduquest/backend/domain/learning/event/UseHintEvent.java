package com.eduquest.backend.domain.learning.event;

import java.util.UUID;

public record UseHintEvent(
        Long memberId,
        UUID problemUuid,
        Integer level
) {

    public static UseHintEvent of(Long memberId, UUID problemUuid, Integer level) {
        return new UseHintEvent(memberId, problemUuid, level);
    }

}
