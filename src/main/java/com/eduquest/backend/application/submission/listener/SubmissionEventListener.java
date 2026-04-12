package com.eduquest.backend.application.submission.listener;

import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import com.eduquest.backend.domain.reward.event.GrantPointEvent;
import com.eduquest.backend.domain.submission.event.SubmissionEvaluatedEvent;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SubmissionEventListener {

    private final ApplicationEventPublisher eventPublisher;
    private final StageQueryService stageQueryService;
    private final MemberQueryService memberQueryService;

    @Async("evaluationTaskExecutor")
    @TransactionalEventListener
    public void handleSubmissionEvaluated(SubmissionEvaluatedEvent event) {

        if (Boolean.TRUE.equals(event.isCorrect())) {
            if ("basic".equals(event.problemType())) {
                // 기본 문제의 경우, 정답이 맞더라도 포인트를 지급하지 않음
            } else {
                // 최종 문제인 경우 스테이지 클리어 보상을 지급함
                UUID stageUuid = event.stageUuid();
                Long stageId = stageQueryService.findIdByUuid(stageUuid);
                Long amount = stageQueryService.findRewardById(stageId);

                Member member = memberQueryService.findMemberById(event.memberId());

                eventPublisher.publishEvent(GrantPointEvent.of(member.getId(), amount, "submission:" + event.submissionId()));
            }
            // 오답 처리(오답노트 생성)는 별도의 비동기 이벤트 리스너에서 처리합니다.
        }
    }
}

