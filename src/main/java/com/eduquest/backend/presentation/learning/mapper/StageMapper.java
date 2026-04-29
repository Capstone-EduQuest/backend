package com.eduquest.backend.presentation.learning.mapper;

import com.eduquest.backend.application.learning.dto.StageDto;
import com.eduquest.backend.application.learning.dto.StageListDto;
import com.eduquest.backend.presentation.learning.dto.response.StageListResponse;
import com.eduquest.backend.presentation.learning.dto.response.StageResponse;

import java.util.List;
import java.util.stream.Collectors;

public final class StageMapper {

    private StageMapper() {}

    public static StageResponse toResponse(StageDto dto) {
        return StageResponse.of(dto.uuid(), dto.title(), dto.number(), dto.reward());
    }

    public static List<StageResponse> toResponseList(List<StageDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(StageMapper::toResponse).collect(Collectors.toList());
    }

    public static StageListResponse toListResponse(StageListDto dto) {
        return StageListResponse.of(dto.page(), dto.size(), dto.sort(), dto.isAsc(), toResponseList(dto.results()));
    }
}

