package com.eduquest.backend.application.submission.service;

import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.domain.submission.event.EvaluationReadyEvent;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.service.SubmissionCommandService;
import com.eduquest.backend.domain.submission.service.SubmissionQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {

	private final ProblemQueryService problemQueryService;
	private final MemberQueryService memberQueryService;

	private final SubmissionCommandService submissionCommandService;
	private final SubmissionQueryService submissionQueryService;
	private final ApplicationEventPublisher eventPublisher;


	@Transactional
	public UUID submit(UUID problemUuid, String userId, String answer) {

		ProblemQuery.Detail detail = problemQueryService.findProblemByUuid(problemUuid);

		Long problemId = detail.id();

		// userId(String) -> userUuid(UUID) -> memberId(Long)
		UUID userUuid = memberQueryService.findMemberUuidByUserId(userId);
		Long memberId = memberQueryService.findMemberIdByUuid(userUuid);

		// domain 모델 생성 후 도메인 포트로 저장
		Submission submissionDomain = Submission.of(memberId, problemId, answer);
		Long submissionId = submissionCommandService.saveSubmission(submissionDomain);

		// 저장된 Submission을 조회하여 UUID를 획득
		Submission savedSubmission = submissionQueryService.findById(submissionId);
		UUID submissionUuid = savedSubmission.getUuid();

		// 평가 요청 이벤트 발행 (ApplicationEventPublisher 사용)
		eventPublisher.publishEvent(EvaluationReadyEvent.of(this, submissionUuid));

		log.info("Submission stored and evaluation enqueued: submissionId={}, submissionUuid={}, memberId={}", submissionId, submissionUuid, memberId);

		return submissionUuid;

	}

}


