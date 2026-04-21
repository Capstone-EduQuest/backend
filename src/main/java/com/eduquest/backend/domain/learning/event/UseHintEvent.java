package com.eduquest.backend.domain.learning.event;

import java.util.UUID;

public record UseHintEvent(
        Long memberId,
        Long hintId
) {

    public static UseHintEvent of(Long memberId, Long hindId) {
        return new UseHintEvent(memberId, hindId);
    }

}
