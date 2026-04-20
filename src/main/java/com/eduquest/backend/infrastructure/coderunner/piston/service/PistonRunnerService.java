package com.eduquest.backend.infrastructure.coderunner.piston.service;

import com.eduquest.backend.domain.submission.dto.request.CodeEvaluateRequest;
import com.eduquest.backend.domain.submission.dto.response.CodeEvaluateResponse;
import com.eduquest.backend.domain.submission.service.CodeRunnerService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Value("${coderunner.piston.url}")
    private String baseUrl;
    @Value("${coderunner.piston.scheme}")
    private String scheme;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public CodeEvaluateResponse evaluate(CodeEvaluateRequest evaluateRequest) {
        try {

            PistonRequest pistonRequest = PistonRequest.builder()
                    .stdin("")
                    .language(evaluateRequest.language())
                    .version(evaluateRequest.version())
                    .files(List.of(Map.of("name", evaluateRequest.fileName(), "content", evaluateRequest.source())))
                    .compileMemoryLimit(evaluateRequest.memoryLimitKb())
                    .compileTimeout(evaluateRequest.timeLimitMs())
                    .build();

            RestClient restClient = createRestClient();

            ResponseEntity<String> results = restClient.post()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(pistonRequest)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        log.error("Client error when calling Piston API: " + request.getURI().toString() + " - " + response.getStatusText());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        log.error("Client error when calling Piston API: " + request.getURI().toString() + " - " + response.getStatusText());
                    }).toEntity(String.class);

            return parseResponse(results);
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
            @JsonProperty("compile_timeout")
            Long compileTimeout,
            @JsonProperty("compile_memory_limit")
            Long compileMemoryLimit,
            @JsonProperty("run_timeout")
            Long runTimeout,
            @JsonProperty("run_memory_limit")
            Long runMemoryLimit
    ) {

    }

    private RestClient createRestClient() {

        /* piston에서 HTTP/2를 지원하지 않으므로 HTTP/2 cleartext(h2c) 업그레이드 비활성화
        https:// (TLS/암호화 연결)에서는 ALPN을 통해 HTTP/2 협상이 일어나고, 서버가 HTTP/2를 지원하지 않으면 자동으로 HTTP/1.1로 fallback됨
        하지만 piston은 http로 연결하므로 HTTP/2 미지원 하면 바로 Bad Request 반환
        따라서 자바 표준 HTTP Client(JdkClientHttpRequestFactory)에서 HTTP/1.1을 강제하도록 설정 */
        if (scheme.equals("http")) {
            return RestClient.builder().baseUrl("http://" + baseUrl)
                    .requestFactory(new JdkClientHttpRequestFactory(
                            HttpClient.newBuilder()
                                    .version(HttpClient.Version.HTTP_1_1)
                                    .build()
                    ))
                    .build();
        } else {
            return RestClient.builder().baseUrl(scheme + "://" + baseUrl).build();
        }

    }

    private CodeEvaluateResponse parseResponse(ResponseEntity<String> results) {

        JsonNode root = null;
        try {
            root = mapper.readTree(results.getBody() == null ? "{}" : results.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

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

    }

}
