package com.eduquest.backend.infrastructure.persistence.submission.service;

import com.eduquest.backend.domain.submission.service.WrongNoteCommandService;
import com.eduquest.backend.infrastructure.persistence.submission.entity.WrongNoteEntity;
import com.eduquest.backend.infrastructure.persistence.submission.repository.WrongNoteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JpaWrongNoteCommandService implements WrongNoteCommandService {

    private final WrongNoteJpaRepository wrongNoteJpaRepository;

    @Override
    @Transactional
    public void createWrongNote(String answer, Long memberId) {
        WrongNoteEntity wrongNote = WrongNoteEntity.of(answer, null, Boolean.FALSE, null, memberId);
        wrongNoteJpaRepository.save(wrongNote);
    }
}

