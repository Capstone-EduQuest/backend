package com.eduquest.backend.domain.community.service;

import com.eduquest.backend.domain.community.model.Answer;

import java.util.List;
import java.util.UUID;

public interface AnswerQueryService {

    Answer findAnswerById(Long id);

    Answer findAnswerByUuid(UUID uuid);

    List<Answer> findAnswersByQuestionId(Long questionId);

    List<Answer> findAnswersByQuestionUuid(UUID questionUuid);

}

