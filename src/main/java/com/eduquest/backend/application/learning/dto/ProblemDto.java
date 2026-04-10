package com.eduquest.backend.application.learning.dto;

import java.util.List;
import java.util.UUID;

public record ProblemDto(
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
		List<HintDto> hints
) {

	public static ProblemDto of(UUID uuid, UUID stageUuid, String stageTitle, Integer stageNumber, String type, Integer number, String summary, String example, String expectedOutput, String block, List<HintDto> hints) {
		return new ProblemDto(uuid, stageUuid, stageTitle, stageNumber, type, number, summary, example, expectedOutput, block, hints);
	}

}


