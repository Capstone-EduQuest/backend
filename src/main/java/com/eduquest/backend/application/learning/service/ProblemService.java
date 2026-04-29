package com.eduquest.backend.application.learning.service;

import com.eduquest.backend.application.learning.dto.HintDto;
import com.eduquest.backend.application.learning.dto.ProblemCommand;
import com.eduquest.backend.application.learning.dto.ProblemDto;
import com.eduquest.backend.application.learning.dto.ProblemListDto;
import com.eduquest.backend.application.learning.exception.LearningErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.domain.learning.event.UseHintEvent;
import com.eduquest.backend.domain.learning.model.Hint;
import com.eduquest.backend.domain.learning.model.Problem;
import com.eduquest.backend.domain.learning.service.HintHistoryQueryService;
import com.eduquest.backend.domain.learning.service.ProblemCommandService;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.domain.reward.event.GrantPointEvent;
import com.eduquest.backend.domain.reward.service.WalletQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final StageQueryService stageQueryService;
    private final ProblemCommandService problemCommandService;
    private final ProblemQueryService problemQueryService;
    private final HintHistoryQueryService hintHistoryQueryService;
    private final MemberQueryService  memberQueryService;
    private final WalletQueryService walletQueryService;
    private final ApplicationEventPublisher eventPublisher;

    public void createProblem(ProblemCommand command) {

        List<Hint> domainHints = command.hints() == null ? List.of() : command.hints().stream()
                .map(h -> Hint.of(h.level(), h.point(), h.content()))
                .collect(Collectors.toList());

        UUID stageUuid = UUID.fromString(command.stageUuid());
        Long stageId = stageQueryService.findIdByUuid(stageUuid);

        Problem problem = Problem.of(stageId, command.type(), command.number(), command.summary(), command.example(), command.expectedOutput(), command.block(), domainHints);

        problemCommandService.saveProblem(problem);

    }

    public void updateProblem(UUID uuid, ProblemCommand command) {

        ProblemQuery.Detail detail = problemQueryService.findProblemByUuid(uuid);

        List<Hint> domainHints = command.hints() == null ? List.of() : command.hints().stream()
                .map(h -> Hint.of(h.level(), h.point(), h.content()))
                .collect(Collectors.toList());

        Long stageId = stageQueryService.findIdByUuid(UUID.fromString(command.stageUuid()));

        Problem problem = Problem.of(detail.uuid(), detail.id(), stageId, command.type(), command.number(), command.summary(), command.example(), command.expectedOutput(), command.block(), domainHints);

        problem.updateProblem(stageId, command.type(), command.number(), command.summary(), command.example(), command.expectedOutput(), command.block());
        problem.updateHints(domainHints);

        problemCommandService.updateProblem(problem);

    }

    public void deleteProblem(UUID uuid) {
        problemCommandService.deleteProblem(uuid);
    }

    public ProblemDto getProblem(UUID uuid) {

        ProblemQuery.Detail detail = problemQueryService.findProblemByUuid(uuid);

        List<HintDto> hintList = detail.hints() == null ? List.of() : detail.hints().stream()
                .map(h -> HintDto.of(h.level(), h.point(), h.content()))
                .collect(Collectors.toList());

        return ProblemDto.of(
                detail.uuid(),
                detail.stageUuid(),
                detail.stageTitle(),
                detail.stageNumber(),
                detail.type(),
                detail.number(),
                detail.summary(),
                detail.example(),
                detail.expectedOutput(),
                detail.block(),
                hintList
        );
    }

    public List<ProblemDto> findProblemsByStageNumber(Integer stageNumber) {
        List<ProblemQuery.Detail> details = problemQueryService.findAllDetailsByStageNumber(stageNumber);

        return details.stream().map(detail -> {
            List<HintDto> hintList = detail.hints() == null ? List.of() : detail.hints().stream()
                    .map(h -> HintDto.of(h.level(), h.point(), h.content()))
                    .collect(Collectors.toList());

            return ProblemDto.of(
                    detail.uuid(),
                    detail.stageUuid(),
                    detail.stageTitle(),
                    detail.stageNumber(),
                    detail.type(),
                    detail.number(),
                    detail.summary(),
                    detail.example(),
                    detail.expectedOutput(),
                    detail.block(),
                    hintList
            );
        }).collect(Collectors.toList());
    }

    public ProblemListDto listProblems(int page, int size, String sort, Boolean isAsc) {
        return ProblemListDto.of(page, size, sort, isAsc, List.of());
    }

    @Transactional(readOnly = true)
    public HintDto findHint(UUID problemUuid, Integer level, String userId) {

        ProblemQuery.HintDetail hintDetail = problemQueryService.findHintByProblemUuidAndLevel(problemUuid, level);
        Long memberId = memberQueryService.findMemberIdByUserId(userId);

        HintDto hintDto = HintDto.of(
                hintDetail.level(),
                hintDetail.point(),
                hintDetail.content()
        );

        boolean isHintHistoryExists = hintHistoryQueryService.isHintHistoryExistsByHintIdAndMemberId(hintDetail.id(), memberId);

        if (!isHintHistoryExists && walletQueryService.findByUserId(memberId).getBalance() >= hintDto.point()) {
            // 힌트 포인트 차감 이벤트 발생시키기
            eventPublisher.publishEvent(
                    GrantPointEvent.of(
                            memberId,
                            -hintDto.point(),
                            "힌트 사용 - 문제번호 : " + hintDetail.problemId() + " - 힌트 레벨 " + hintDto.level()
                    )
            );

            // 힌트 기록 이벤트 발생시키기
            eventPublisher.publishEvent(
                    UseHintEvent.of(
                            memberId,
                            hintDetail.id()
                    )
            );

        } else if (!isHintHistoryExists) {
            // 포인트 부족
            throw new EduQuestException(LearningErrorCode.INSUFFICIENT_BALANCE);
        }

        return hintDto;

    }

}

