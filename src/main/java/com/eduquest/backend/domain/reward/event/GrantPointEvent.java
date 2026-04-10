package com.eduquest.backend.domain.reward.event;

public record GrantPointEvent(
        Long memberId,
        Long point,
        String reason
) {
}
