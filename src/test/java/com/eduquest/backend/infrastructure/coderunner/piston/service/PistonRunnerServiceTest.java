package com.eduquest.backend.infrastructure.coderunner.piston.service;

import com.eduquest.backend.domain.submission.dto.request.CodeEvaluateRequest;
import com.eduquest.backend.domain.submission.dto.response.CodeEvaluateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.config.location=file:src/main/resources/application.yml,file:src/main/resources/application-dev.yml")
@ActiveProfiles("dev")
class PistonRunnerServiceTest {

    @Autowired
    PistonRunnerService pistonRunnerService;

    @Test
    void evaluate_success() {

        // given
        CodeEvaluateRequest req = new CodeEvaluateRequest(
                "print('Hello, world!')",
                "python",
                "3.12.0",
                "main.py",
                "null",
                0L,
                0L,
                false
        );

        // when
        CodeEvaluateResponse resp = pistonRunnerService.evaluate(req);

        // then
        assertEquals("python", resp.language());
        assertNotNull(resp.stdout());
        assertTrue(resp.stdout().contains("Hello"));
        assertNotEquals(Integer.valueOf(-1), resp.exitCode());

    }

    @Test
    void evaluate_fail_compile_error() {

        // given
        CodeEvaluateRequest req = new CodeEvaluateRequest(
                "print('Hello' ", // 문법 오류 유도
                "python",
                "3.12.0",
                "main.py",
                "null",
                0L,
                0L,
                false
        );

        // when
        CodeEvaluateResponse resp = pistonRunnerService.evaluate(req);

        // then
        assertEquals("python", resp.language());
        // 컴파일/런타임 에러 메시지가 compileStderr 또는 stderr 중 하나에 기록될 수 있음
        boolean hasSyntaxInCompile = resp.compileStderr() != null && resp.compileStderr().contains("SyntaxError");
        boolean hasSyntaxInRun = resp.stderr() != null && resp.stderr().contains("SyntaxError");
        assertTrue(hasSyntaxInCompile || hasSyntaxInRun, "Expected SyntaxError in compileStderr or stderr");

    }

    @Test
    void evaluate_fail_runtime_error() {

        // given
        CodeEvaluateRequest req = new CodeEvaluateRequest(
                "print(1/0)",
                "python",
                "3.12.0",
                "main.py",
                "null",
                0L,
                0L,
                false
        );

        // when
        CodeEvaluateResponse resp = pistonRunnerService.evaluate(req);

        // then
        assertEquals("python", resp.language());
        assertNotNull(resp.stderr());
        assertTrue(resp.stderr().contains("ZeroDivisionError") || resp.stderr().contains("division by zero"));
        assertNotEquals(Integer.valueOf(-1), resp.exitCode());

    }

}