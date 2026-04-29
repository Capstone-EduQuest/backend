package com.eduquest.backend.presentation.learning.dto.response;

import com.eduquest.backend.application.learning.dto.HintDto;

public record HintResponse(
		Integer level,
		Long point,
		String content
) {

	public static HintResponse of(HintDto dto) {
		return new HintResponse(dto.level(), dto.point(), dto.content());
	}

}


