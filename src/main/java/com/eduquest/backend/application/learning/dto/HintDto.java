package com.eduquest.backend.application.learning.dto;

public record HintDto(
		Integer level,
		Long point,
		String content
) {

	public static HintDto of(Integer level, Long point, String content) {
		return new HintDto(level, point, content);
	}

}


