package com.eduquest.backend.application.learning.dto;

import java.util.List;

public record ProblemCommand(
        String stageUuid,
        String type,
        Integer number,
        String summary,
        String example,
        String expectedOutput,
        String block,
        List<HintDto> hints
) {

    public static ProblemCommand of(String stageUuid, String type, Integer number, String summary, String example, String expectedOutput, String block, List<HintDto> hints) {
        return new ProblemCommand(stageUuid, type, number, summary, example, expectedOutput, block, hints);
    }

}

