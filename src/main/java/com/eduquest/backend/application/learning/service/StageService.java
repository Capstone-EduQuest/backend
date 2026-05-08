package com.eduquest.backend.application.learning.service;

import com.eduquest.backend.application.learning.dto.StageDto;
import com.eduquest.backend.application.learning.dto.StageListDto;
import com.eduquest.backend.domain.learning.dto.StageQuery;
import com.eduquest.backend.domain.learning.model.Stage;
import com.eduquest.backend.domain.learning.service.StageCommandService;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StageService {

    private final StageQueryService stageQueryService;
    private final StageCommandService stageCommandService;

    public void createStage(String title, Integer number, Long reward) {

        Stage stage = Stage.of(title, number, reward);

        stageCommandService.saveStage(stage);
    }

    public void updateStage(UUID uuid, String title, Integer number, Long reward) {

        Stage stage = stageQueryService.findStageByUuid(uuid);

        stage.update(title, number, reward);

        stageCommandService.updateStage(stage);

    }

    public void deleteStage(UUID uuid) {
        stageCommandService.deleteStage(uuid);
    }

    public StageDto getStage(UUID uuid) {

        Stage stage = stageQueryService.findStageByUuid(uuid);

        return StageDto.of(uuid, stage.getTitle(), stage.getNumber(), stage.getReward());
    }

    public StageListDto listStages(int page, int size, String sort, Boolean isAsc) {

        List<StageQuery.Summary> summariesWithReward = stageQueryService.findStageSummariesWithRewardByPagination(page, size, sort, isAsc);

        if (summariesWithReward == null || summariesWithReward.isEmpty()) {
            return StageListDto.of(page, size, sort, isAsc, List.of());
        }

        List<StageDto> stageDtoList = new ArrayList<>();

        for (StageQuery.Summary summary : summariesWithReward) {
            stageDtoList.add(StageDto.of(summary.stageUuid(), summary.title(), summary.number(), summary.reward()));
        }

        return StageListDto.of(page, size, sort, isAsc, stageDtoList);
    }

}

