package com.eduquest.backend.domain.learning.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class HintHistory {

    private Long id;
    private LocalDateTime createdAt;
    private Long hintId;
    private Long memberId;

    @Builder(access = AccessLevel.PROTECTED)
    public HintHistory(Long id, LocalDateTime createdAt, Long hintId, Long memberId) {
        this.id = id;
        this.createdAt = createdAt;
        this.hintId = hintId;
        this.memberId = memberId;
    }

    public static HintHistory of(Long id, LocalDateTime createdAt, Long hintId, Long memberId) {
        return HintHistory.builder()
                .id(id)
                .createdAt(createdAt)
                .hintId(hintId)
                .memberId(memberId)
                .build();
    }

}
