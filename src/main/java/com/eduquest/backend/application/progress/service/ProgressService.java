package com.eduquest.backend.application.progress.service;

import com.eduquest.backend.application.progress.dto.ProgressDto;
import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import com.eduquest.backend.domain.progress.dto.ProgressQuery;
import com.eduquest.backend.domain.submission.model.Evaluation;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.service.EvaluationQueryService;
import com.eduquest.backend.domain.submission.service.SubmissionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final StageQueryService stageQueryService;
    private final ProblemQueryService problemQueryService;
    private final MemberQueryService memberQueryService;
    private final SubmissionQueryService submissionQueryService;
    private final EvaluationQueryService evaluationQueryService;

    public List<ProgressDto.ProgressItem> findByUserUuid(UUID userUuid, String requesterUserId) {

        Long userId = memberQueryService.findMemberIdByUuid(userUuid);

        List<ProgressQuery.Detail> stages = stageQueryService.findAllStageSummaries();

        List<Submission> submissions = submissionQueryService.findByUserId(userId);

        Map<Long, Long> submissionToProblemMap = submissions.stream()
                .filter(s -> s.getId() != null)
                .collect(Collectors.toMap(Submission::getId, Submission::getProblemId));

        List<Long> submissionIds = new ArrayList<>(submissionToProblemMap.keySet());

        List<Evaluation> evaluations = evaluationQueryService.findBySubmissionIds(submissionIds);

        Set<Long> clearedProblemIds = evaluations.stream()
                .filter(e -> Boolean.TRUE.equals(e.getIsCorrect()))
                .map(Evaluation::getSubmissionId)
                .map(submissionToProblemMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<ProgressDto.ProgressItem> results = new ArrayList<>();

        for (ProgressQuery.Detail stage : stages) {
            Integer stageNumber = stage.stageNumber();
            List<ProblemQuery.Detail> problems = problemQueryService.findAllDetailsByStageNumber(stageNumber);

            List<Integer> clearedNumbers = problems.stream()
                    .filter(p -> clearedProblemIds.contains(p.id()))
                    .map(ProblemQuery.Detail::number)
                    .collect(Collectors.toList());

            results.add(new ProgressDto.ProgressItem(stage.stageTitle(), stage.totalQuestionCount().intValue(), clearedNumbers));
        }

        return results;
    }

}

