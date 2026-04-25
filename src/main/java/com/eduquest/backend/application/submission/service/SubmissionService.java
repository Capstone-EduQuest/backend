package com.eduquest.backend.application.submission.service;

import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.domain.submission.dto.request.CodeEvaluateRequest;
import com.eduquest.backend.domain.submission.dto.response.CodeEvaluateResponse;
import com.eduquest.backend.domain.submission.event.SubmissionEvaluatedEvent;
import com.eduquest.backend.domain.submission.event.WrongNoteCreateRequestedEvent;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.model.enums.EvaluationResult;
import com.eduquest.backend.domain.submission.service.CodeRunnerService;
import com.eduquest.backend.domain.submission.service.SubmissionCommandService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {

	@Value("${coderunner.config.language.python.version}")
	private String languageVersion;
	@Value("${coderunner.config.language.python.file}")
	private String fileName;
	@Value("${coderunner.config.limit.compilation.time}")
	private String compileTimeLimitMs;
	@Value("${coderunner.config.limit.compilation.memory}")
	private String compileTimeMemoryLimitKb;
	@Value("${coderunner.config.limit.runtime.time}")
	private String runTimeLimitMs;
	@Value("${coderunner.config.limit.runtime.memory}")
	private String runtTimeMemoryLimitKb;

	private static final String DEFAULT_LANGUAGE = "python";
	private static final int LOG_TRUNCATE_MAX = 2000;

	private final ProblemQueryService problemQueryService;
	private final MemberQueryService memberQueryService;
	private final StageQueryService stageQueryService;
	private final SubmissionCommandService submissionCommandService;
	private final ApplicationEventPublisher eventPublisher;
	private final CodeRunnerService codeRunnerService;
	private final ObjectMapper objectMapper;

	@Transactional
	public boolean submit(UUID problemUuid, String userId, String answer) {

		ProblemQuery.Detail detail = problemQueryService.findProblemByUuid(problemUuid);

		Long problemId = detail.id();

		// userId(String) -> userUuid(UUID) -> memberId(Long)
		UUID userUuid = memberQueryService.findMemberUuidByUserId(userId);
		Long memberId = memberQueryService.findMemberIdByUuid(userUuid);

		// domain 모델 생성 후 도메인 포트로 저장
		Submission submissionDomain = Submission.of(memberId, problemId, answer);
		Long submissionId = submissionCommandService.saveSubmission(submissionDomain);

		boolean isWrong = false;

		if ("basic".equalsIgnoreCase(Objects.toString(detail.type(), ""))) {
			isWrong = !compareAnswers(detail.expectedOutput(), answer);
		} else {

			EvaluationResult result = evaluateCodeProblem(detail, answer);

			isWrong = result == EvaluationResult.WRONG;

		}

		log.info("Evaluated submission: submissionId={}, memberId={}, isWrong={}", submissionId, memberId, isWrong);

		SubmissionEvaluatedEvent event = SubmissionEvaluatedEvent.of(
				submissionId,
				memberId,
                !isWrong,
				detail.stageUuid(),
				detail.type()
		);

		eventPublisher.publishEvent(event);

		// 틀렸을 경우에만 오답노트 생성 요청 이벤트 발행(비동기 처리)
		if (isWrong) {
			WrongNoteCreateRequestedEvent wrongNoteEvent = WrongNoteCreateRequestedEvent.of(submissionId, memberId, problemId, answer);
			eventPublisher.publishEvent(wrongNoteEvent);
		}

		return !isWrong;
	}

	private EvaluationResult evaluateCodeProblem(ProblemQuery.Detail detail, String source) {

		String language = DEFAULT_LANGUAGE;
		String input = "";
		Boolean compileOnly = false;

		CodeEvaluateRequest request = CodeEvaluateRequest.of(
				source,
				language,
				languageVersion,
				fileName,
				input,
				Long.parseLong(compileTimeLimitMs),
				Long.parseLong(compileTimeMemoryLimitKb),
				Long.parseLong(runTimeLimitMs),
				Long.parseLong(runtTimeMemoryLimitKb),
				compileOnly
		);

		CodeEvaluateResponse evaluateResponse;

		try {
			evaluateResponse = codeRunnerService.evaluate(request);
		} catch (Exception ex) {
			log.error("Code runner evaluation failed for problem {}: {}", detail.id(), ex.getMessage(), ex);
			return EvaluationResult.ERROR;
		}

		if (evaluateResponse == null) {
			log.warn("Code runner returned null response for problem {}", detail.id());
			return EvaluationResult.ERROR;
		}

		try {
			log.info("Code evaluation summary for problemId={}: language={}, version={}, exitCode={}, signal={}, timedOut={}, compileExitCode={}",
					detail.id(), evaluateResponse.language(), evaluateResponse.version(), evaluateResponse.exitCode(), evaluateResponse.signal(), evaluateResponse.timedOut(), evaluateResponse.compileExitCode());
		} catch (Exception e) {
			log.warn("Failed to log evaluation details for problem {}: {}", detail.id(), e.getMessage());
		}

		// 컴파일 실패
		if (evaluateResponse.compileExitCode() != null && evaluateResponse.compileExitCode() != 0) {
			log.info("Code compile error for problem {} : {}", detail.id(), truncateLog(evaluateResponse.compileStderr(), 500));
			return EvaluationResult.WRONG;
		}

		// 타임아웃
		if (Boolean.TRUE.equals(evaluateResponse.timedOut())) {
			log.info("Code timed out for problem {}: {}", detail.id(), truncateLog(evaluateResponse.stdout(), 200));
			return EvaluationResult.WRONG;
		}

		// 런타임 에러(비정상 종료)
		if (evaluateResponse.exitCode() != null && evaluateResponse.exitCode() != 0) {
			log.info("Code runtime non-zero exit for problem {}: exit={}, stderr={}", detail.id(), evaluateResponse.exitCode(), truncateLog(evaluateResponse.stderr(), 500));
			return EvaluationResult.WRONG;
		}

		String expected = detail.expectedOutput();
		String stdout = evaluateResponse.stdout() == null ? "" : evaluateResponse.stdout();

		boolean compare = compareAnswers(expected, stdout);
		return compare ? EvaluationResult.CORRECT : EvaluationResult.WRONG;

	}

	private boolean compareAnswers(String expected, String answer) {
		if (expected == null)
			return false;
		if (answer == null)
			return false;

		String expNorm = normalizeNewlines(expected).trim();
		String ansNorm = normalizeNewlines(answer).trim();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode expectedNode = objectMapper.readTree(expNorm);
			JsonNode answerNode = objectMapper.readTree(ansNorm);
			return expectedNode.equals(answerNode);
		} catch (Exception e) {
			// JSON 파싱 실패 시 문자열 비교(공백/개행 정규화된 값)
			return expNorm.equals(ansNorm);
		}
	}

	// helper: normalize newlines to avoid issues with different platforms (CRLF vs LF)
	private String normalizeNewlines(String s) {
		return s == null ? null : s.replace("\r\n", "\n");
	}

	// helper: truncate long logs to avoid huge log records
	private String truncateLog(String s, int max) {
		if (s == null) return null;
		if (s.length() <= max) return s;
		return s.substring(0, max) + "...(truncated, total " + s.length() + " chars)";
	}

}


