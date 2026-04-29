package com.eduquest.backend.domain.community.service;

import com.eduquest.backend.domain.community.model.Question;

import java.util.UUID;

public interface QuestionCommandService {

    Long saveQuestion(Question question);

    void deleteQuestionByUuid(UUID uuid);

    void markAdoptedByUuid(UUID questionUuid, UUID answerUuid);

}

