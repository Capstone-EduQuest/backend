package com.eduquest.backend.application.submission.listener;

import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import com.eduquest.backend.domain.reward.event.GrantPointEvent;
import com.eduquest.backend.domain.submission.event.SubmissionEvaluatedEvent;
import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionEntity;
import com.eduquest.backend.infrastructure.persistence.submission.entity.WrongNoteEntity;
import com.eduquest.backend.infrastructure.persistence.submission.repository.SubmissionJpaRepository;
import com.eduquest.backend.infrastructure.persistence.submission.repository.WrongNoteJpaRepository;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SubmissionEventListener {

    private final ApplicationEventPublisher eventPublisher;
    private final StageQueryService stageQueryService;
    private final WrongNoteJpaRepository wrongNoteJpaRepository;
    private final MemberQueryService memberQueryService;
    private final SubmissionJpaRepository submissionJpaRepository;

    @Async
    @Transactional
    @EventListener
    public void handleSubmissionEvaluated(SubmissionEvaluatedEvent event) {

        if (Boolean.TRUE.equals(event.isCorrect())) {
            UUID stageUuid = event.stageUuid();
            Long stageId = stageQueryService.findIdByUuid(stageUuid);
            Long amount = stageQueryService.findRewardById(stageId);

            Member member = memberQueryService.findMemberById(event.memberId());

            eventPublisher.publishEvent(new GrantPointEvent(member.getId(), amount, "submission:" + event.submissionId()));
        } else {
            SubmissionEntity submissionEntity = submissionJpaRepository.findById(event.submissionId()).orElse(null);
            String answer = submissionEntity != null ? submissionEntity.getAnswer() : null;

            WrongNoteEntity wrongNote = WrongNoteEntity.of(answer, null, Boolean.FALSE, null, event.memberId());
            wrongNoteJpaRepository.save(wrongNote);
        }
    }

}

