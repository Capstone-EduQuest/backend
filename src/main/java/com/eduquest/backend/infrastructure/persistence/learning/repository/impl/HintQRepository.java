package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

import java.util.Optional;
import java.util.UUID;

public interface HintQRepository {

    Optional<Long> findIdByProblemUuidAndLevel(UUID problemUuid, int level);

}
