package com.eduquest.backend.infrastructure.coderunner.piston.service;

import com.eduquest.backend.domain.submission.dto.request.CodeEvaluateRequest;
import com.eduquest.backend.domain.submission.dto.response.CodeEvaluateResponse;
import com.eduquest.backend.domain.submission.service.CodeRunnerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
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
    public CodeEvaluateResponse evaluate(CodeEvaluateRequest evaluateRequest) {
        try {

            PistonRequest pistonRequest = PistonRequest.builder()
                    .stdin("")
                    .language(evaluateRequest.language())
                    .version("3.12.0")
                    .files(List.of(Map.of("name", "main.py", "content", evaluateRequest.source())))
                    .compile_memory_limit(-1)
                    .compile_timeout(10000)
                    .build();

            // piston에서 http/2를 지원하지 않으므로 HTTP/2 cleartext(h2c) 업그레이드 비활성화
            RestClient restClient = RestClient.builder().baseUrl("http://localhost:2000")
                    .requestFactory(new JdkClientHttpRequestFactory(
                            HttpClient.newBuilder()
                                    .version(HttpClient.Version.HTTP_1_1)
                                    .build()
                    ))
                    .build();

            ResponseEntity<String> results = restClient.post()
                    .uri(baseUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(pistonRequest)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        log.info(request.getURI().toString());
                        log.info(response.getStatusText());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        log.info(request.getURI().toString());
                        log.info(response.getStatusText());
                    }).toEntity(String.class);

            log.info(results.getBody());

            JsonNode root = mapper.readTree(results.getBody() == null ? "{}" : results.getBody());

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

    @Builder
    private record PistonRequest(
            String language,
            String version,
            List<Map<String, String>> files,
            String stdin,
            Integer compile_timeout,
            Integer compile_memory_limit,
            Integer run_timeout,
            Integer run_memory_limit
    ) {

    }

}
