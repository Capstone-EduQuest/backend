package com.eduquest.backend.application.submission.service;

import com.eduquest.backend.domain.submission.event.SubmissionEvaluatedEvent;
import com.eduquest.backend.infrastructure.persistence.submission.entity.EvaluationEntity;
import com.eduquest.backend.infrastructure.persistence.submission.entity.SubmissionEntity;
import com.eduquest.backend.infrastructure.persistence.submission.mapper.SubmissionEntityMapper;
import com.eduquest.backend.infrastructure.persistence.submission.repository.EvaluationJpaRepository;
import com.eduquest.backend.infrastructure.persistence.submission.repository.SubmissionJpaRepository;
import com.eduquest.backend.infrastructure.persistence.submission.repository.WrongNoteJpaRepository;
import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmissionService {

	private final ProblemQueryService problemQueryService;
	private final MemberQueryService memberQueryService;
	private final StageQueryService stageQueryService;
	private final SubmissionJpaRepository submissionJpaRepository;
	private final EvaluationJpaRepository evaluationJpaRepository;
	private final WrongNoteJpaRepository wrongNoteJpaRepository;
	private final SubmissionEntityMapper submissionEntityMapper;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public boolean submit(UUID problemUuid, String userId, String answer) {

		ProblemQuery.Detail detail = problemQueryService.findProblemByUuid(problemUuid);

		Long problemId = detail.id();

		// userId(String) -> userUuid(UUID) -> memberId(Long)
		UUID userUuid = memberQueryService.findMemberUuidByUserId(userId);
		Long memberId = memberQueryService.findMemberIdByUuid(userUuid);

		SubmissionEntity submission = submissionEntityMapper.toEntity(memberId, problemId, answer);
		submission = submissionJpaRepository.save(submission);

		boolean isCorrect = false;

		if (detail.type() != null && "basic".equalsIgnoreCase(detail.type())) {
			isCorrect = compareAnswers(detail.expectedOutput(), answer);
		} else {
			// final type or others: to be implemented with code runner integration
			isCorrect = false;
		}

		EvaluationEntity evaluation = EvaluationEntity.of(isCorrect, submission.getId());
		evaluationJpaRepository.save(evaluation);

		// publish domain event for evaluated submission — post-processing (reward, wrong-note) handled asynchronously
		eventPublisher.publishEvent(
				SubmissionEvaluatedEvent.of(
						submission.getId(),
						memberId,
						isCorrect,
						detail.stageUuid(),
						detail.type()
				)
		);

		return isCorrect;
	}

	private boolean compareAnswers(String expected, String answer) {
		if (expected == null) {
			return false;
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode expectedNode = mapper.readTree(expected.trim());
			JsonNode answerNode = mapper.readTree(answer.trim());
			return expectedNode.equals(answerNode);
		} catch (Exception e) {
			return expected.trim().equals(answer.trim());
		}
	}

}


