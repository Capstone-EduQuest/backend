package com.eduquest.backend.presentation.learning.dto.response;

import java.util.List;
import java.util.UUID;

public record ProblemResponse(
        UUID uuid,
        UUID stageUuid,
        String stageTitle,
        Integer stageNumber,
        String type,
        Integer number,
        String summary,
        String example,
        String expectedOutput,
        String block,
        List<HintResponse> hints
) {

    public static ProblemResponse of(UUID uuid, UUID stageUuid, String stageTitle, Integer stageNumber, String type, Integer number, String summary, String example, String expectedOutput, String block, List<HintResponse> hints) {
        return new ProblemResponse(uuid, stageUuid, stageTitle, stageNumber, type, number, summary, example, expectedOutput, block, hints);
    }

}

