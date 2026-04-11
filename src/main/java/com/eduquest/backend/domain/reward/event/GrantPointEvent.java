package com.eduquest.backend.domain.reward.event;

public record GrantPointEvent(
        Long memberId,
        Long point,
        String reason
) {

    public static GrantPointEvent of(Long memberId, Long point, String reason) {
        return new GrantPointEvent(memberId, point, reason);
    }

}
