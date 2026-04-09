package com.eduquest.backend.presentation.learning.mapper;

import com.eduquest.backend.application.learning.dto.HintDto;
import com.eduquest.backend.presentation.learning.dto.request.HintRequest;
import com.eduquest.backend.presentation.learning.dto.response.HintResponse;

import java.util.List;
import java.util.stream.Collectors;

public final class HintMapper {

    private HintMapper() {}

    public static HintDto toDto(HintRequest req) {
        return HintDto.of(req.level(), req.point(), req.content());
    }

    public static List<HintDto> toDtoList(List<HintRequest> reqs) {
        if (reqs == null) return List.of();
        return reqs.stream().map(HintMapper::toDto).collect(Collectors.toList());
    }

    public static HintResponse toResponse(HintDto dto) {
        return HintResponse.of(dto);
    }

    public static List<HintResponse> toResponseList(List<HintDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(HintMapper::toResponse).collect(Collectors.toList());
    }
}

