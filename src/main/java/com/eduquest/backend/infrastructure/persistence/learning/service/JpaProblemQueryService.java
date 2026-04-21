package com.eduquest.backend.infrastructure.persistence.learning.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.learning.repository.HintQueryRepository;
import com.eduquest.backend.infrastructure.persistence.learning.repository.ProblemQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaProblemQueryService implements ProblemQueryService {

	private final ProblemQueryRepository problemQueryRepository;
	private final HintQueryRepository hintQueryRepository;

	@Override
	public ProblemQuery.Detail findProblemByUuid(UUID uuid) {
		return problemQueryRepository.findByUuid(uuid)
				.orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));
	}

	@Override
	public Long findHintIdByProblemUuidAndLevel(UUID problemUuid, int level) {
		return hintQueryRepository.findIdByProblemUuidAndLevel(problemUuid, level)
				.orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));
	}

	@Override
	public List<ProblemQuery.Summary> findAllByStageNumber(Integer stageNumber) {
		return problemQueryRepository.findAllByStageNumber(stageNumber);
	}

	@Override
	public List<ProblemQuery.Detail> findAllDetailsByStageNumber(Integer stageNumber) {
		return problemQueryRepository.findDetailsByStageNumber(stageNumber);
	}

}


