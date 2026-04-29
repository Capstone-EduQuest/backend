package com.eduquest.backend.domain.learning.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Stage {

    private Long id;
    private UUID uuid;
    private String title;
    private Integer number;
    private Long reward;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(access = AccessLevel.PROTECTED)
    public Stage(String title, Integer number, Long reward) {
        this.title = title;
        this.number = number;
        this.reward = reward;
    }

    public static Stage of(String title, Integer number, Long reward) {
        return Stage.builder()
                .title(title)
                .number(number)
                .reward(reward)
                .build();
    }

}

