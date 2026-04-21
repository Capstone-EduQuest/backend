package com.eduquest.backend.infrastructure.persistence.learning.repository.impl;

public interface HintHistoryQRepository {

    boolean isExistsByHintIdAndMemberId(Long hintId, Long memberId);

}
