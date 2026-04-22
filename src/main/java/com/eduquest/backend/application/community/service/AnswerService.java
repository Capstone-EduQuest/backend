
package com.eduquest.backend.application.community.service;

import com.eduquest.backend.application.community.dto.AnswerListQuery;
import com.eduquest.backend.application.community.dto.AnswerListResult;
import com.eduquest.backend.application.community.dto.CreateAnswerCommand;
import com.eduquest.backend.application.community.exception.CommunityErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.community.event.AnswerAdoptedEvent;
import com.eduquest.backend.domain.community.model.Answer;
import com.eduquest.backend.domain.community.model.Question;
import com.eduquest.backend.domain.community.service.AnswerCommandService;
import com.eduquest.backend.domain.community.service.AnswerQueryService;
import com.eduquest.backend.domain.community.service.QuestionCommandService;
import com.eduquest.backend.domain.community.service.QuestionQueryService;
import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final ApplicationEventPublisher eventPublisher;
    private final AnswerCommandService answerCommandService;
    private final AnswerQueryService answerQueryService;
    private final QuestionQueryService questionQueryService;
    private final QuestionCommandService questionCommandService;
    private final MemberQueryService memberQueryService;

    private static final long ADOPT_REWARD = 100L;

    public void createAnswer(CreateAnswerCommand command) {
        if (command == null || command.userId() == null || command.userId().isBlank()) {
            throw new EduQuestException(CommunityErrorCode.INVALID_REQUEST);
        }

        Long memberId = memberQueryService.findMemberIdByUserId(command.userId());

        Question question = questionQueryService.findQuestionByUuid(command.questionUuid());
        if (question == null) {
            throw new EduQuestException(CommunityErrorCode.QUESTION_NOT_FOUND);
        }

        Answer answer = Answer.of(command.content(), memberId, question.getId());

        Long savedId = answerCommandService.saveAnswer(answer);

        Answer saved = answerQueryService.findAnswerById(savedId);

        if (saved == null || saved.getUuid() == null) {
            throw new EduQuestException(CommunityErrorCode.INVALID_REQUEST);
        }

    }

    public AnswerListResult findAnswersByQuestionUuid(UUID questionUuid, AnswerListQuery query) {
        List<Answer> answers = answerQueryService.findAnswersByQuestionUuid(questionUuid);

        List<AnswerListResult.Item> items = answers.stream().map(a -> {
            Member member = memberQueryService.findMemberById(a.getUserId());

            return AnswerListResult.Item.of(a.getUuid(), a.getContent(), member.getUuid(), member.getNickname(), a.getIsAdopted(), a.getCreatedAt());
        }).collect(Collectors.toList());

        return AnswerListResult.of(query.page(), query.size(), null, query.isAsc(), items);
    }

    public void deleteAnswerByUuid(UUID answerUuid) {
        answerCommandService.deleteAnswerByUuid(answerUuid);
    }

    @Transactional
    public void adoptAnswer(UUID answerUuid, String requesterUserId) {
        // only question author can adopt; admin not allowed
        if (requesterUserId == null || requesterUserId.isBlank()) {
            throw new EduQuestException(CommunityErrorCode.INVALID_REQUEST);
        }

        Answer answer = answerQueryService.findAnswerByUuid(answerUuid);
        if (answer == null) {
            throw new EduQuestException(CommunityErrorCode.ANSWER_NOT_FOUND);
        }

        Question question = questionQueryService.findQuestionById(answer.getCommunityPostId());
        if (question == null) {
            throw new EduQuestException(CommunityErrorCode.QUESTION_NOT_FOUND);
        }

        Long requesterMemberId = memberQueryService.findMemberIdByUserId(requesterUserId);

        // only the original question author may adopt
        if (requesterMemberId == null || !requesterMemberId.equals(question.getUserId())) {
            throw new EduQuestException(CommunityErrorCode.FORBIDDEN);
        }

        // cannot adopt an answer written by the question author
        if (answer.getUserId() != null && answer.getUserId().equals(question.getUserId())) {
            throw new EduQuestException(CommunityErrorCode.INVALID_REQUEST);
        }

        // 수행: 도메인에게 채택 명령
        questionCommandService.markAdoptedByUuid(question.getUuid(), answer.getUuid());
        answerCommandService.adoptAnswerByUuid(answerUuid);

        // publish domain event for reward handling
        AnswerAdoptedEvent event = AnswerAdoptedEvent.of(answerUuid, ADOPT_REWARD);
        eventPublisher.publishEvent(event);
    }

}

