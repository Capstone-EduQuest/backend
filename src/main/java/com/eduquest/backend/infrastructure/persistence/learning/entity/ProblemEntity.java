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
@Table(name = "problem")
public class ProblemEntity extends BasicUpdateEntity {

    @Column(name = "stage_id", nullable = false)
    private Long stageId;

    @Column(name = "type", nullable = false, length = 64)
    private String type;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "summary", nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(name = "example", nullable = false, columnDefinition = "TEXT")
    private String example;

    @Column(name = "expected_output", nullable = false, columnDefinition = "TEXT")
    private String expectedOutput;

    // DDL: block JSON NULL
    @Column(name = "block", columnDefinition = "JSON")
    private String block;

    @Builder(access = AccessLevel.PROTECTED)
    public ProblemEntity(Long stageId, String type, Integer number, String summary, String example, String expectedOutput, String block) {
        this.stageId = stageId;
        this.type = type;
        this.number = number;
        this.summary = summary;
        this.example = example;
        this.expectedOutput = expectedOutput;
        this.block = block;
    }

    public static ProblemEntity of(Long stageId, String type, Integer number, String summary, String example, String expectedOutput, String block) {
        return ProblemEntity.builder()
                .stageId(stageId)
                .type(type)
                .number(number)
                .summary(summary)
                .example(example)
                .expectedOutput(expectedOutput)
                .block(block)
                .build();
    }

}

