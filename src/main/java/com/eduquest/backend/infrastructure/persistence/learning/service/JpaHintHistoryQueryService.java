package com.eduquest.backend.infrastructure.persistence.learning.service;

import com.eduquest.backend.domain.learning.service.HintHistoryQueryService;
import com.eduquest.backend.infrastructure.persistence.learning.repository.HintHistoryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaHintHistoryQueryService implements HintHistoryQueryService {

    private final HintHistoryQueryRepository hintHistoryQueryRepository;

    @Override
    public boolean isHintHistoryExistsByHintIdAndMemberId(Long hintId, Long memberId) {
        return hintHistoryQueryRepository.isExistsByHintIdAndMemberId(hintId, memberId);
    }
}
