package com.eduquest.backend.domain.learning.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem {

    private Long id;
    private UUID uuid;
    private Long stageId;
    private String type;
    private Integer number;
    private String summary;
    private String example;
    private String expectedOutput;
    private String block;
    private List<Hint> hints;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(access = AccessLevel.PROTECTED)
    public Problem(Long stageId, String type, Integer number, String summary, String example, String expectedOutput, String block, List<Hint> hints) {
        this.stageId = stageId;
        this.type = type;
        this.number = number;
        this.summary = summary;
        this.example = example;
        this.expectedOutput = expectedOutput;
        this.block = block;
        this.hints = hints;
    }

    public static Problem of(Long stageId, String type, Integer number, String summary, String example, String expectedOutput, String block, List<Hint> hints) {
        return Problem.builder()
                .stageId(stageId)
                .type(type)
                .number(number)
                .summary(summary)
                .example(example)
                .expectedOutput(expectedOutput)
                .block(block)
                .hints(hints)
                .build();
    }

    public static Problem of(UUID uuid, Long id, Long stageId, String type, Integer number, String summary, String example, String expectedOutput, String block, List<Hint> hints) {
        Problem p = Problem.of(stageId, type, number, summary, example, expectedOutput, block, hints);
        p.uuid = uuid;
        p.id = id;
        return p;
    }

    public void updateProblem(Long stageId, String type, Integer number, String summary, String example, String expectedOutput, String block) {
        this.stageId = stageId;
        this.type = type;
        this.number = number;
        this.summary = summary;
        this.example = example;
        this.expectedOutput = expectedOutput;
        this.block = block;
    }

    public void updateHints(List<Hint> hints) {
        this.hints = hints == null ? List.of() : hints;
    }

}

