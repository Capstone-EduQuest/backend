package com.eduquest.backend.infrastructure.persistence.learning.service;

import com.eduquest.backend.domain.learning.model.HintHistory;
import com.eduquest.backend.domain.learning.service.HintHistoryCommandService;
import com.eduquest.backend.infrastructure.persistence.learning.mapper.HintHistoryMapper;
import com.eduquest.backend.infrastructure.persistence.learning.repository.HintHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaHintHistoryCommandService implements HintHistoryCommandService {

    private final HintHistoryMapper historyMapper;
    private final HintHistoryJpaRepository historyJpaRepository;

    @Override
    public Long saveHintHistory(HintHistory hintHistory) {
        return historyJpaRepository.save(historyMapper.toEntity(hintHistory)).getId();
    }
}
