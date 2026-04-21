package com.eduquest.backend.application.community.service;

import com.eduquest.backend.application.community.dto.CreateQuestionCommand;
import com.eduquest.backend.application.community.dto.QuestionDetailResponse;
import com.eduquest.backend.application.community.dto.QuestionListQuery;
import com.eduquest.backend.application.community.dto.QuestionListResult;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QuestionService {

    public UUID createQuestion(CreateQuestionCommand command) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void deleteQuestionByUuid(UUID questionUuid) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public QuestionListResult findQuestions(QuestionListQuery query) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public QuestionDetailResponse findQuestionByUuid(UUID questionUuid) {
        throw new UnsupportedOperationException("Not implemented");
    }

}

