package com.eduquest.backend.presentation.learning.mapper;

import com.eduquest.backend.application.learning.dto.ProblemCommand;
import com.eduquest.backend.application.learning.dto.ProblemDto;
import com.eduquest.backend.application.learning.dto.ProblemListDto;
import com.eduquest.backend.presentation.learning.dto.request.ProblemCreateRequest;
import com.eduquest.backend.presentation.learning.dto.request.ProblemUpdateRequest;
import com.eduquest.backend.presentation.learning.dto.response.ProblemListResponse;
import com.eduquest.backend.presentation.learning.dto.response.ProblemResponse;

import java.util.List;
import java.util.stream.Collectors;

public final class ProblemMapper {

    private ProblemMapper() {}

    public static ProblemResponse toResponse(ProblemDto dto) {
        return ProblemResponse.of(
                dto.uuid(), dto.stageUuid(), dto.stageTitle(), dto.stageNumber(), dto.type(), dto.number(), dto.summary(), dto.example(), dto.expectedOutput(), dto.block(), HintMapper.toResponseList(dto.hints())
        );
    }

    public static List<ProblemResponse> toResponseList(List<ProblemDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream().map(ProblemMapper::toResponse).collect(Collectors.toList());
    }

    public static ProblemListResponse toListResponse(ProblemListDto dto) {
        return ProblemListResponse.of(dto.page(), dto.size(), dto.sort(), dto.isAsc(), toResponseList(dto.results()));
    }

    public static ProblemCommand toCommand(ProblemCreateRequest req) {
        return ProblemCommand.of(req.stageUuid(), req.type(), req.number(), req.summary(), req.example(), req.expectedOutput(), req.block(), HintMapper.toDtoList(req.hints()));
    }

    public static ProblemCommand toCommand(ProblemUpdateRequest req) {
        return ProblemCommand.of(req.stageUuid(), req.type(), req.number(), req.summary(), req.example(), req.expectedOutput(), req.block(), HintMapper.toDtoList(req.hints()));
    }
}

