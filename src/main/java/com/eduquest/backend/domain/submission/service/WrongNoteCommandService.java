package com.eduquest.backend.domain.submission.service;


public interface WrongNoteCommandService {

    void createWrongNote(String answer, Long memberId);
}

