package com.eduquest.backend.domain.community.service;

import com.eduquest.backend.domain.community.model.Answer;

import java.util.UUID;

public interface AnswerCommandService {

    Long saveAnswer(Answer answer);

    void deleteAnswerByUuid(UUID uuid);

    void adoptAnswerByUuid(UUID answerUuid);

}

