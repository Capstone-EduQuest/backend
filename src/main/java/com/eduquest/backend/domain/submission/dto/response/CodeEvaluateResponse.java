package com.eduquest.backend.domain.submission.dto.response;

/**
 * Response DTO returned from the code-runner service (Piston v2).
 */
public record CodeEvaluateResponse(
		String language,
		String version,
		String stdout,
		String stderr,
		Integer exitCode,
		String signal,
		Boolean timedOut,
		String compileStdout,
		String compileStderr,
		Integer compileExitCode
) {

	public static CodeEvaluateResponse of(String language, String version,
										 String stdout, String stderr, Integer exitCode, String signal, Boolean timedOut,
										 String compileStdout, String compileStderr, Integer compileExitCode) {
		return new CodeEvaluateResponse(language, version, stdout, stderr, exitCode, signal, timedOut, compileStdout, compileStderr, compileExitCode);
	}

}
