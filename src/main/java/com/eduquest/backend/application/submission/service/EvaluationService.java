package com.eduquest.backend.application.submission.service;

import com.eduquest.backend.application.submission.dto.EvaluationInfo;
import com.eduquest.backend.application.submission.exception.SubMissionErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.domain.submission.model.Evaluation;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.model.enums.SubmissionStatus;
import com.eduquest.backend.domain.submission.service.EvaluationQueryService;
import com.eduquest.backend.domain.submission.service.SubmissionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final SubmissionQueryService submissionQueryService;
    private final MemberQueryService memberQueryService;
    private final EvaluationQueryService evaluationQueryService;

    @Transactional(readOnly = true)
    public EvaluationInfo findBySubmissionUuid(UUID submissionUuid, String userId) {

        Long memberId = memberQueryService.findMemberIdByUserId(userId);

        Submission submission = submissionQueryService.findSubmissionByUuid(submissionUuid);
        SubmissionStatus submissionStatus = submissionQueryService.findSubmissionStatusBySubmissionId(submission.getId());

        if (!submission.getUserId().equals(memberId)) {
            throw new EduQuestException(SubMissionErrorCode.FORBIDDEN_SUBMISSION_ACCESS);
        }

        switch (submissionStatus) {
            case PENDING, PROCESSING, RETRYING: throw new EduQuestException(SubMissionErrorCode.EVALUATION_PENDING);
            case FAILED : throw new EduQuestException(SubMissionErrorCode.EVALUATION_FAILED);
            case SUCCEEDED: break;
        }

        Evaluation evaluation = evaluationQueryService.findBySubmissionId(submission.getId());

        return EvaluationInfo.of(evaluation.getIsCorrect(), evaluation.getCreatedAt());

    }
}

