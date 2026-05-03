package com.eduquest.backend.application.submission.service;

import com.eduquest.backend.application.submission.dto.EvaluationInfo;
import com.eduquest.backend.application.submission.exception.SubMissionErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.domain.submission.model.Evaluation;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.service.EvaluationQueryService;
import com.eduquest.backend.domain.submission.service.SubmissionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        Evaluation evaluation = evaluationQueryService.findBySubmissionId(submission.getId());

        return EvaluationInfo.of(evaluation.getIsCorrect(), evaluation.getCreatedAt());
    }
}

