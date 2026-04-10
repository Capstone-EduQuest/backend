package com.eduquest.backend.application.reward.dto;

import java.util.List;

public record RewardListDto(
		int page,
		int size,
		String sort,
		Boolean isAsc,
		List<RewardHistoryDto> results
) {
	public static RewardListDto of(int page, int size, String sort, Boolean isAsc, List<RewardHistoryDto> results) {
		return new RewardListDto(page, size, sort, isAsc, results);
	}
}


