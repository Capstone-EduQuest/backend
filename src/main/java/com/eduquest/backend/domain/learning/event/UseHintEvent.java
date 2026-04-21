package com.eduquest.backend.domain.learning.event;

public record UseHintEvent(
        Long memberId,
        Long hintId
) {

    public static UseHintEvent of(Long memberId, Long hindId) {
        return new UseHintEvent(memberId, hindId);
    }

}
