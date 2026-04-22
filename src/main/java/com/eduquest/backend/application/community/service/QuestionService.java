
package com.eduquest.backend.application.community.service;

import com.eduquest.backend.application.community.dto.CreateQuestionCommand;
import com.eduquest.backend.application.community.dto.QuestionDetailResponse;
import com.eduquest.backend.application.community.dto.QuestionListQuery;
import com.eduquest.backend.application.community.dto.QuestionListResult;
import com.eduquest.backend.application.community.exception.CommunityErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.community.model.Question;
import com.eduquest.backend.domain.community.service.QuestionCommandService;
import com.eduquest.backend.domain.community.service.QuestionQueryService;
import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionCommandService questionCommandService;
    private final QuestionQueryService questionQueryService;
    private final MemberQueryService memberQueryService;

    public void createQuestion(CreateQuestionCommand command) {

        if (command == null || command.userId() == null || command.userId().isBlank()) {
            throw new EduQuestException(CommunityErrorCode.INVALID_REQUEST);
        }

        Long memberId = memberQueryService.findMemberIdByUserId(command.userId());

        Question question = Question.of(command.title(), command.content(), memberId);

        Long savedId = questionCommandService.saveQuestion(question);

        Question saved = questionQueryService.findQuestionById(savedId);

        if (saved == null || saved.getUuid() == null) {
            throw new EduQuestException(CommunityErrorCode.INVALID_REQUEST);
        }

    }

    public void deleteQuestionByUuid(UUID questionUuid) {
        questionCommandService.deleteQuestionByUuid(questionUuid);
    }

    public QuestionListResult findQuestions(QuestionListQuery query) {
        List<Question> questions = questionQueryService.findAll(query.page(), query.size());

        List<QuestionListResult.Item> items = questions.stream().map(q -> {
            Member member = memberQueryService.findMemberById(q.getUserId());
            UUID userUuid = member.getUuid();
            String nickname = member.getNickname();
            LocalDateTime createdAt = q.getCreatedAt() == null ? LocalDateTime.now() : q.getCreatedAt();

            return QuestionListResult.Item.of(q.getUuid(), q.getTitle(), userUuid, nickname, createdAt);
        }).collect(Collectors.toList());

        return QuestionListResult.of(query.page(), query.size(), query.sort(), query.isAsc(), items);
    }

    public QuestionDetailResponse findQuestionByUuid(UUID questionUuid) {
        Question question = questionQueryService.findQuestionByUuid(questionUuid);

        if (question == null) {
            throw new EduQuestException(CommunityErrorCode.QUESTION_NOT_FOUND);
        }

        Member member = memberQueryService.findMemberById(question.getUserId());

        LocalDateTime createdAt = question.getCreatedAt() == null ? LocalDateTime.now() : question.getCreatedAt();

        return QuestionDetailResponse.of(
                question.getUuid(),
                question.getTitle(),
                member.getUuid(),
                member.getNickname(),
                createdAt,
                question.getContent(),
                question.getIsAdopted(),
                null
        );
    }

}

