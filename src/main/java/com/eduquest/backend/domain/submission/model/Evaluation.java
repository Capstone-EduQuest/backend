package com.eduquest.backend.domain.submission.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Evaluation {

	private Long id;
	private UUID uuid;
	private Long submissionId;
	private Boolean isCorrect;
	private LocalDateTime createdAt;

	@Builder(access = AccessLevel.PROTECTED)
	public Evaluation(Long submissionId, Boolean isCorrect) {
		this.submissionId = submissionId;
		this.isCorrect = isCorrect;
	}

	public static Evaluation of(Long submissionId, Boolean isCorrect) {
		return Evaluation.builder()
				.submissionId(submissionId)
				.isCorrect(isCorrect)
				.build();
	}

	public static Evaluation of(UUID uuid, Long id, Long submissionId, Boolean isCorrect, LocalDateTime createdAt) {
		Evaluation e = Evaluation.of(submissionId, isCorrect);
		e.uuid = uuid;
		e.id = id;
		e.createdAt = createdAt;
		return e;
	}

}


