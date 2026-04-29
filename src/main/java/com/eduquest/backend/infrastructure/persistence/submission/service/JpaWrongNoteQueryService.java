package com.eduquest.backend.infrastructure.persistence.submission.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.submission.dto.WrongNoteQuery;
import com.eduquest.backend.domain.submission.model.WrongNote;
import com.eduquest.backend.domain.submission.service.WrongNoteQueryService;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.submission.mapper.WrongNoteEntityMapper;
import com.eduquest.backend.infrastructure.persistence.submission.repository.WrongNoteQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaWrongNoteQueryService implements WrongNoteQueryService {

    private final WrongNoteQueryRepository wrongNoteQueryRepository;
    private final WrongNoteEntityMapper mapper;

    @Override
    public WrongNoteQuery.Detail findWrongDetailNoteByUserIdAndProblemId(Long userId, Long problemId) {
        return wrongNoteQueryRepository.findByUserIdAndProblemId(userId, problemId)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));
    }

    @Override
    public List<WrongNoteQuery.Detail> findWrongDetailNotesByUserId(Long userId, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(direction, sortBy));

        return wrongNoteQueryRepository.findByUserId(userId, page, size, sortBy, isAsc);
    }

    @Override
    public WrongNoteQuery.Detail findWrongDetailNoteByUuid(UUID wrongNoteUuid) {
        return wrongNoteQueryRepository.findDetailByUuid(wrongNoteUuid)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));
    }

    @Override
    public WrongNote findWrongNoteByUuid(UUID wrongNoteUuid) {
        return mapper.toDomain(wrongNoteQueryRepository.findByUuid(wrongNoteUuid)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA)));
    }

    @Override
    public List<WrongNoteQuery.Detail> findWrongNotes(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(direction, sortBy));

        return wrongNoteQueryRepository.findByPagination(page, size, sortBy, isAsc);
    }

    @Override
    public long countWrongNotes() {
        return wrongNoteQueryRepository.count();
    }

    @Override
    public long countWrongNotesByUserId(Long userId) {
        return wrongNoteQueryRepository.countByUserId(userId);
    }
}

