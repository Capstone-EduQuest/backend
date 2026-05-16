package com.eduquest.backend.infrastructure.persistence.submission.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.service.SubmissionQueryService;
import com.eduquest.backend.infrastructure.persistence.submission.exception.SubmissionDatabaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.submission.mapper.SubmissionEntityMapper;
import com.eduquest.backend.infrastructure.persistence.submission.repository.SubmissionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaSubmissionQueryService implements SubmissionQueryService {

	private final SubmissionQueryRepository submissionQueryRepository;
	private final SubmissionEntityMapper mapper;

	@Override
	public Submission findSubmissionById(Long id) {
		return submissionQueryRepository.findById(id)
				.map(mapper::toDomain)
				.orElseThrow(() -> new EduQuestException(SubmissionDatabaseErrorCode.SUBMISSION_NOT_FOUND));
	}

	@Override
	public List<Submission> findSubmissionsByProblemId(Long problemId) {
		return mapper.toDomainList(submissionQueryRepository.findByProblemId(problemId));
	}

	@Override
	public List<Submission> findSubmissionsByUserId(Long userId) {
		return mapper.toDomainList(submissionQueryRepository.findByUserId(userId));
	}

	@Override
	public Submission findSubmissionByUuid(java.util.UUID uuid) {
		return submissionQueryRepository.findByUuid(uuid)
				.map(mapper::toDomain)
				.orElseThrow(() -> new EduQuestException(SubmissionDatabaseErrorCode.SUBMISSION_NOT_FOUND));
	}

}


