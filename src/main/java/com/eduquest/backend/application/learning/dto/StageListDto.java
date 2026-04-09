package com.eduquest.backend.application.learning.dto;

import java.util.List;

public record StageListDto(
		int page,
		int size,
		String sort,
		Boolean isAsc,
		List<StageDto> results
) {
	public static StageListDto of(int page, int size, String sort, Boolean isAsc, List<StageDto> results) {
		return new StageListDto(page, size, sort, isAsc, results);
	}
}


