package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

import com.eduquest.backend.domain.learning.dto.ProblemQuery;

import java.util.Optional;
import java.util.UUID;

public interface HintQRepository {

    Optional<Long> findIdByProblemUuidAndLevel(UUID problemUuid, int level);

    Optional<ProblemQuery.HintDetail> findHintDetailByProblemUuidAndLevel(UUID problemUuid, Integer level);

}
