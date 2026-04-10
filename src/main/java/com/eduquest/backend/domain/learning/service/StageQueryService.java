package com.eduquest.backend.domain.learning.service;

import java.util.UUID;

public interface StageQueryService {

    Long findIdByUuid(UUID uuid);

    /**
     * 스테이지의 보상 금액을 조회한다.
     * @param stageId 스테이지 PK
     * @return 보상 금액
     */
    Long findRewardById(Long stageId);

}

