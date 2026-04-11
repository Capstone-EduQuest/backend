package com.eduquest.backend.application.submission.listener;

import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import com.eduquest.backend.domain.reward.event.GrantPointEvent;
import com.eduquest.backend.domain.submission.event.SubmissionEvaluatedEvent;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.service.SubmissionQueryService;
import com.eduquest.backend.domain.submission.service.WrongNoteCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SubmissionEventListener {

    private final ApplicationEventPublisher eventPublisher;
    private final StageQueryService stageQueryService;
    private final WrongNoteCommandService wrongNoteCommandService;
    private final MemberQueryService memberQueryService;
    private final SubmissionQueryService submissionQueryService;

    @Async
    @Transactional
    @TransactionalEventListener
    public void handleSubmissionEvaluated(SubmissionEvaluatedEvent event) {

        if (Boolean.TRUE.equals(event.isCorrect())) {
            UUID stageUuid = event.stageUuid();
            Long stageId = stageQueryService.findIdByUuid(stageUuid);
            Long amount = stageQueryService.findRewardById(stageId);

            Member member = memberQueryService.findMemberById(event.memberId());

            eventPublisher.publishEvent(GrantPointEvent.of(member.getId(), amount, "submission:" + event.submissionId()));
        } else {
            Submission submission = submissionQueryService.findById(event.submissionId());
            String answer = submission != null ? submission.getAnswer() : null;

            wrongNoteCommandService.createWrongNote(answer, event.memberId());
        }
    }

}

