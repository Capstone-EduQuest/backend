package com.eduquest.backend.application.submission.service;

import com.eduquest.backend.domain.learning.dto.ProblemQuery;
import com.eduquest.backend.domain.learning.service.ProblemQueryService;
import com.eduquest.backend.domain.learning.service.StageQueryService;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import com.eduquest.backend.domain.submission.dto.request.CodeEvaluateRequest;
import com.eduquest.backend.domain.submission.dto.response.CodeEvaluateResponse;
import com.eduquest.backend.domain.submission.event.SubmissionEvaluatedEvent;
import com.eduquest.backend.domain.submission.event.WrongNoteCreateRequestedEvent;
import com.eduquest.backend.domain.submission.model.Submission;
import com.eduquest.backend.domain.submission.service.CodeRunnerService;
import com.eduquest.backend.domain.submission.service.SubmissionCommandService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {

	private static final String DEFAULT_LANGUAGE = "python3";
	// 권장 기본값: 0L은 무제한이므로 안전한 값으로 교체하거나 config화 권장
	private static final long DEFAULT_TIME_LIMIT_MS = 2000L;
	private static final long DEFAULT_MEMORY_LIMIT_KB = 64 * 1024L;
	private static final int LOG_TRUNCATE_MAX = 2000;

	private final ProblemQueryService problemQueryService;
	private final MemberQueryService memberQueryService;
	private final StageQueryService stageQueryService;
	private final SubmissionCommandService submissionCommandService;
	private final ApplicationEventPublisher eventPublisher;
	private final CodeRunnerService codeRunnerService;

	@Transactional
	public boolean submit(UUID problemUuid, String userId, String answer) {

		ProblemQuery.Detail detail = problemQueryService.findProblemByUuid(problemUuid);

		Long problemId = detail.id();

		// userId(String) -> userUuid(UUID) -> memberId(Long)
		UUID userUuid = memberQueryService.findMemberUuidByUserId(userId);
		Long memberId = memberQueryService.findMemberIdByUuid(userUuid);

		// domain 모델 생성 후 도메인 포트로 저장 (infrastructure 의존 제거)
		Submission submissionDomain = Submission.of(memberId, problemId, answer);
		Long submissionId = submissionCommandService.saveSubmission(submissionDomain);

		boolean isCorrect;

		if ("basic".equalsIgnoreCase(Objects.toString(detail.type(), ""))) {
			isCorrect = compareAnswers(detail.expectedOutput(), answer);
		} else {
			isCorrect = evaluateCodeProblem(detail, answer);
		}

		log.info("Evaluated submission: submissionId={}, memberId={}, isCorrect={}", submissionId, memberId, isCorrect);

		SubmissionEvaluatedEvent event = SubmissionEvaluatedEvent.of(
				submissionId,
				memberId,
				isCorrect,
				detail.stageUuid(),
				detail.type()
		);

		eventPublisher.publishEvent(event);

		// 틀렸을 경우에만 오답노트 생성 요청 이벤트 발행(비동기 처리)
		if (!isCorrect) {
			WrongNoteCreateRequestedEvent wrongNoteEvent = WrongNoteCreateRequestedEvent.of(submissionId, memberId, problemId, answer);
			eventPublisher.publishEvent(wrongNoteEvent);
		}

		return isCorrect;
	}

	private boolean evaluateCodeProblem(ProblemQuery.Detail detail, String source) {
		String language = DEFAULT_LANGUAGE;
		String input = "";
		Long timeLimitMs = DEFAULT_TIME_LIMIT_MS;
		Long memoryLimitKb = DEFAULT_MEMORY_LIMIT_KB;
		Boolean compileOnly = false;

		CodeEvaluateRequest request = new CodeEvaluateRequest(
				source,
				language,
				input,
				timeLimitMs,
				memoryLimitKb,
				compileOnly
		);

		CodeEvaluateResponse resp;
		try {
			resp = codeRunnerService.evaluate(request);
		} catch (Exception ex) {
			log.error("Code runner evaluation failed for problem {}: {}", detail.id(), ex.getMessage(), ex);
			return false;
		}

		if (resp == null) {
			log.warn("Code runner returned null response for problem {}", detail.id());
			return false;
		}

		try {
			String stdoutLog = truncateLog(resp.stdout(), LOG_TRUNCATE_MAX);
			String stderrLog = truncateLog(resp.stderr(), LOG_TRUNCATE_MAX);
			String compileStdoutLog = truncateLog(resp.compileStdout(), LOG_TRUNCATE_MAX);
			String compileStderrLog = truncateLog(resp.compileStderr(), LOG_TRUNCATE_MAX);

			log.info("Code evaluation summary for problemId={}: language={}, version={}, exitCode={}, signal={}, timedOut={}, compileExitCode={}",
					detail.id(), resp.language(), resp.version(), resp.exitCode(), resp.signal(), resp.timedOut(), resp.compileExitCode());

			if (stdoutLog != null && !stdoutLog.isBlank()) log.debug("Evaluation stdout (truncated):\n{}", stdoutLog);
			if (stderrLog != null && !stderrLog.isBlank()) log.debug("Evaluation stderr (truncated):\n{}", stderrLog);
			if (compileStdoutLog != null && !compileStdoutLog.isBlank()) log.debug("Compile stdout (truncated):\n{}", compileStdoutLog);
			if (compileStderrLog != null && !compileStderrLog.isBlank()) log.debug("Compile stderr (truncated):\n{}", compileStderrLog);
		} catch (Exception logEx) {
			log.warn("Failed to log evaluation details for problem {}: {}", detail.id(), logEx.getMessage());
		}

		// 컴파일 실패
		if (resp.compileExitCode() != null && resp.compileExitCode() != 0) {
			log.info("Code compile error for problem {} : {}", detail.id(), truncateLog(resp.compileStderr(), 500));
			return false;
		}

		// 타임아웃
		if (Boolean.TRUE.equals(resp.timedOut())) {
			log.info("Code timed out for problem {}: {}", detail.id(), truncateLog(resp.stdout(), 200));
			return false;
		}

		// 런타임 에러(비정상 종료)
		if (resp.exitCode() != null && resp.exitCode() != 0) {
			log.info("Code runtime non-zero exit for problem {}: exit={}, stderr={}", detail.id(), resp.exitCode(), truncateLog(resp.stderr(), 500));
			return false;
		}

		String expected = detail.expectedOutput();
		String stdout = resp.stdout() == null ? "" : resp.stdout();

		return compareAnswers(expected, stdout);
	}

	private boolean compareAnswers(String expected, String answer) {
		if (expected == null) return false;
		if (answer == null) return false;

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


