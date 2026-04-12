package com.eduquest.backend.infrastructure.persistence.submission.service;

import com.eduquest.backend.domain.submission.dto.WrongNoteDto;
import com.eduquest.backend.domain.submission.service.WrongNoteQueryService;
import com.eduquest.backend.infrastructure.persistence.submission.mapper.WrongNoteEntityMapper;
import com.eduquest.backend.infrastructure.persistence.submission.repository.WrongNoteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaWrongNoteQueryService implements WrongNoteQueryService {

    private final WrongNoteJpaRepository wrongNoteJpaRepository;
    private final WrongNoteEntityMapper mapper;

    @Override
    public WrongNoteDto.Detail findWrongNoteByUserIdAndProblemId(Long userId, Long problemId) {
        return wrongNoteJpaRepository.findByUserIdAndProblemId(userId, problemId)
                .map(e -> {
                    var wn = mapper.toDomain(e);
                    return WrongNoteDto.Detail.of(wn.getId(), wn.getUserId(), wn.getProblemId(), wn.getWrongAnswer(), wn.getAiExplanation(), wn.getIsReviewed(), wn.getNextReviewAt(), wn.getCreatedAt(), wn.getUpdatedAt());
                })
                .orElse(null);
    }

    @Override
    public List<WrongNoteDto.Detail> findWrongNotesByUserId(Long userId, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction dir = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(dir, sortBy));

        return wrongNoteJpaRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(e -> {
                    var wn = mapper.toDomain(e);
                    return WrongNoteDto.Detail.of(wn.getId(), wn.getUserId(), wn.getProblemId(), wn.getWrongAnswer(), wn.getAiExplanation(), wn.getIsReviewed(), wn.getNextReviewAt(), wn.getCreatedAt(), wn.getUpdatedAt());
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countWrongNotesByUserId(Long userId) {
        return wrongNoteJpaRepository.countByUserId(userId);
    }
}

