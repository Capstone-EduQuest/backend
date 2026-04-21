package com.eduquest.backend.domain.learning.service;

public interface HintHistoryQueryService {

    boolean isHintHistoryExistsByHintIdAndMemberId(Long hintId, Long memberId);

}
