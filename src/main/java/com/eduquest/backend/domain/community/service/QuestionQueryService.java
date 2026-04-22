package com.eduquest.backend.domain.community.service;

import com.eduquest.backend.domain.community.dto.QuestionQuery;
import com.eduquest.backend.domain.community.model.Question;

import java.util.List;
import java.util.UUID;

public interface QuestionQueryService {

    Question findQuestionById(Long id);

    Question findQuestionByUuid(UUID uuid);

    List<Question> findQuestionsByUserId(Long userId);

    List<QuestionQuery.Summary> findAll(int page, int size);

}

