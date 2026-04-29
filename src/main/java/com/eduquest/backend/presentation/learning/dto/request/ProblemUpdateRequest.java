package com.eduquest.backend.presentation.learning.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProblemUpdateRequest(
		@JsonProperty("stage_uuid")
		@NotBlank(message = "stage UUID는 필수 입니다.") String stageUuid,
		@NotBlank(message = "문제 타입은 필수 입니다.") String type,
		@NotNull(message = "문제 번호는 필수 입니다.") Integer number,
		@NotBlank(message = "요약은 필수 입니다.") String summary,
		@NotBlank(message = "예시는 필수 입니다.") String example,
		@NotBlank(message = "예상 출력은 필수 입니다.") String expectedOutput,
		JsonNode block,
		@NotNull(message = "힌트 목록은 필수 입니다.") List<HintRequest> hints
) {

}


