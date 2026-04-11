package com.eduquest.backend.infrastructure.coderunner.piston.service;

import com.eduquest.backend.domain.submission.dto.request.CodeEvaluateRequest;
import com.eduquest.backend.domain.submission.dto.response.CodeEvaluateResponse;
import com.eduquest.backend.domain.submission.service.CodeRunnerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PistonRunnerService implements CodeRunnerService {

    @Value("${coderunner.piston.url:#{null}}")
    private String baseUrl;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public CodeEvaluateResponse evaluate(CodeEvaluateRequest request) {
        try {

            Map<String, Object> body = new HashMap<>();
            body.put("language", request.language());
            Map<String, String> file = Map.of("name", "Main", "content", request.source() == null ? "" : request.source());
            body.put("files", List.of(file));
            body.put("stdin", request.input() == null ? "" : request.input());
            body.put("args", List.of());
            if (request.timeLimitMs() != null && request.timeLimitMs() > 0) {
                body.put("run_timeout", request.timeLimitMs());
            }
            if (request.memoryLimitKb() != null && request.memoryLimitKb() > 0) {
                body.put("run_memory_limit", request.memoryLimitKb());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String bodyJson = mapper.writeValueAsString(body);

            String resp = RestClient.builder().build().post()
                    .uri(baseUrl)
                    .headers(h -> h.setContentType(MediaType.APPLICATION_JSON))
                    .body(bodyJson, new ParameterizedTypeReference<>() {})
                    .retrieve()
                    .toEntity(String.class)
                    .getBody();
            JsonNode root = mapper.readTree(resp == null ? "{}" : resp);

            String language = root.path("language").asText(null);
            String version = root.path("version").asText(null);

            JsonNode run = root.path("run");
            String stdout = run.path("stdout").asText(null);
            String stderr = run.path("stderr").asText(null);
            Integer exitCode = run.has("code") && !run.get("code").isNull() ? run.get("code").asInt() : null;
            String signal = run.path("signal").isNull() ? null : run.path("signal").asText(null);
            Boolean timedOut = run.has("timed_out") && run.get("timed_out").asBoolean(false);

            JsonNode compile = root.path("compile");
            String compileStdout = compile.path("stdout").asText(null);
            String compileStderr = compile.path("stderr").asText(null);
            Integer compileExit = compile.has("code") && !compile.get("code").isNull() ? compile.get("code").asInt() : null;

            return CodeEvaluateResponse.of(language, version, stdout, stderr, exitCode, signal, timedOut, compileStdout, compileStderr, compileExit);
        } catch (Exception e) {
            log.error("Failed to evaluate code via Piston", e);
            return CodeEvaluateResponse.of(null, null, null, e.getMessage(), -1, null, Boolean.FALSE, null, null, -1);
        }
    }
}
