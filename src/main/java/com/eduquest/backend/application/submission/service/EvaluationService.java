package com.eduquest.backend.application.submission.service;

import com.eduquest.backend.application.submission.dto.EvaluationInfo;
import com.eduquest.backend.application.submission.exception.SubMissionErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.domain.submission.model.Evaluation;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.service.SubmissionQueryService;
import com.eduquest.backend.domain.submission.service.EvaluationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final SubmissionQueryService submissionQueryService;
    private final MemberQueryService memberQueryService;
    private final EvaluationQueryService evaluationQueryService;

    public EvaluationInfo findBySubmissionUuid(UUID submissionUuid, String userId) {
        // userId -> memberId
        UUID userUuid = memberQueryService.findMemberUuidByUserId(userId);
        Long memberId = memberQueryService.findMemberIdByUuid(userUuid);

        Submission submission = submissionQueryService.findByUuid(submissionUuid);

        if (!submission.getUserId().equals(memberId)) {
            throw new EduQuestException(SubMissionErrorCode.FORBIDDEN);
        }

        List<Evaluation> evaluations = evaluationQueryService.findBySubmissionIds(List.of(submission.getId()));
        if (evaluations == null || evaluations.isEmpty()) {
            return null;
        }

        Evaluation evaluation = evaluations.getFirst();
        return EvaluationInfo.of(evaluation.getIsCorrect(), evaluation.getCreatedAt());
    }
}

