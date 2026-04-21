
package com.eduquest.backend.application.community.service;

import com.eduquest.backend.application.community.dto.AnswerListQuery;
import com.eduquest.backend.application.community.dto.AnswerListResult;
import com.eduquest.backend.application.community.dto.CreateAnswerCommand;
import com.eduquest.backend.application.community.event.AdoptConfig;
import com.eduquest.backend.application.community.event.AnswerAdoptedEvent;
import com.eduquest.backend.application.community.exception.CommunityErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.community.model.Answer;
import com.eduquest.backend.domain.community.model.Question;
import com.eduquest.backend.domain.community.service.AnswerCommandService;
import com.eduquest.backend.domain.community.service.AnswerQueryService;
import com.eduquest.backend.domain.community.service.QuestionCommandService;
import com.eduquest.backend.domain.community.service.QuestionQueryService;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    public UUID createAnswer(CreateAnswerCommand command) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new EduQuestException(CommunityErrorCode.INVALID_REQUEST);
        }

        String authUserId = authentication.getName();
        Long memberId = memberQueryService.findMemberIdByUserId(authUserId);

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

        return saved.getUuid();
    }

    public AnswerListResult findAnswersByQuestionUuid(UUID questionUuid, AnswerListQuery query) {
        List<Answer> answers = answerQueryService.findAnswersByQuestionUuid(questionUuid);

        List<AnswerListResult.Item> items = answers.stream().map(a -> {
            var member = memberQueryService.findMemberById(a.getUserId());
            UUID userUuid = member.getUuid();
            String nickname = member.getNickname();
            Instant createdAt = a.getCreatedAt() == null ? Instant.now() : a.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant();

            return AnswerListResult.Item.of(a.getUuid(), a.getContent(), userUuid, nickname, a.getIsAdopted(), createdAt);
        }).collect(Collectors.toList());

        return AnswerListResult.of(query.page(), query.size(), null, query.isAsc(), items);
    }

    public void deleteAnswerByUuid(UUID answerUuid) {
        answerCommandService.deleteAnswerByUuid(answerUuid);
    }

    public void adoptAnswer(UUID answerUuid) {
        // 권한 체크: 질문 작성자 또는 ADMIN
        Answer answer = answerQueryService.findAnswerByUuid(answerUuid);
        if (answer == null) {
            throw new EduQuestException(CommunityErrorCode.ANSWER_NOT_FOUND);
        }

        Question question = questionQueryService.findQuestionById(answer.getCommunityPostId());
        if (question == null) {
            throw new EduQuestException(CommunityErrorCode.QUESTION_NOT_FOUND);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Long requesterMemberId = null;
        if (authentication != null && authentication.getName() != null && !authentication.getName().isBlank()) {
            requesterMemberId = memberQueryService.findMemberIdByUserId(authentication.getName());
        }

        if (!isAdmin && (requesterMemberId == null || !requesterMemberId.equals(question.getUserId()))) {
            throw new EduQuestException(CommunityErrorCode.FORBIDDEN);
        }

        // 수행: 도메인에게 채택 명령
        questionCommandService.markAdoptedByUuid(question.getUuid(), answer.getUuid());
        answerCommandService.adoptAnswerByUuid(answerUuid);

        // 이벤트 발행
        AnswerAdoptedEvent event = AnswerAdoptedEvent.of(answerUuid, AdoptConfig.ADOPT_REWARD);
        eventPublisher.publishEvent(event);
    }

}

