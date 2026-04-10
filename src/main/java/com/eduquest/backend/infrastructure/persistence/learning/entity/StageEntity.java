package com.eduquest.backend.infrastructure.persistence.learning.entity;

import com.eduquest.backend.infrastructure.persistence.common.entity.BasicUpdateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stage")
public class StageEntity extends BasicUpdateEntity {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "reward", nullable = false)
    private Long reward;

    @Builder(access = AccessLevel.PROTECTED)
    public StageEntity(String title, Integer number, Long reward) {
        this.title = title;
        this.number = number;
        this.reward = reward;
    }

    public static StageEntity of(String title, Integer number, Long reward) {
        return StageEntity.builder()
                .title(title)
                .number(number)
                .reward(reward)
                .build();
    }

}

