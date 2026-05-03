package com.eduquest.backend.infrastructure.persistence.submission.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.service.SubmissionQueryService;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
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
	public Submission findById(Long id) {
		return submissionQueryRepository.findById(id)
				.map(mapper::toDomain)
				.orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));
	}

	@Override
	public List<Submission> findByProblemId(Long problemId) {
		return mapper.toDomainList(submissionQueryRepository.findByProblemId(problemId));
	}

	@Override
	public List<Submission> findByUserId(Long userId) {
		return mapper.toDomainList(submissionQueryRepository.findByUserId(userId));
	}

	@Override
	public Submission findByUuid(java.util.UUID uuid) {
		return submissionQueryRepository.findByUuid(uuid)
				.map(mapper::toDomain)
				.orElseThrow(() -> new com.eduquest.backend.common.exception.EduQuestException(com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode.NOT_FOUND_DATA));
	}

}


