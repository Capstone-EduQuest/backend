package com.eduquest.backend.infrastructure.security.filter;

import com.eduquest.backend.infrastructure.security.util.HtmlSanitizeUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class XssRequestFilter extends OncePerRequestFilter {

    /**
     * HTML/JSON 요청에 대해 입력값을 안전하게 정제(sanitize)하여 XSS 공격을 완화하는 서블릿 필터입니다.
     *
     * 동작 원리:
     * - 요청 파라미터는 모두 sanitizeString(..)으로 정제하여 파라미터 맵을 교체합니다.
     * - Content-Type이 application/json인 경우에는 요청 본문을 읽어 JSON 내부의 모든 문자열 값을 재귀적으로 정제한 후
     *   바이트로 다시 저장하여 body를 교체한 요청 래퍼를 생성합니다.
     * - 비 JSON 요청은 파라미터만 교체한 래퍼로 체인을 진행합니다.
     *
     * 주의사항:
     * - 이 필터는 요청 InputStream을 읽어 재구성하므로, 다른 필터/핸들러에서 원본 InputStream에 직접 접근하면 안 됩니다.
     */
    private final HtmlSanitizeUtils htmlSanitizeUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Content-Type 검사: JSON 요청인지 확인
        String contentType = request.getContentType();
        boolean isJsonRequest = contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE);

        // 모든 파라미터 값을 먼저 정제하여 교체할 맵을 준비한다.
        Map<String, String[]> sanitizedParameterMap = sanitizeParameterMap(request);

        HttpServletRequest requestToUse;

        if (isJsonRequest) {
            // JSON 요청인 경우 본문(body)을 모두 읽어와 JSON 내부의 문자열 항목들을 재귀적으로 정제한다.
            // (정제 실패 시에는 원본 body를 사용하여 요청을 포워딩)
            byte[] rawBodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
            String originalBody = new String(rawBodyBytes, StandardCharsets.UTF_8);

            try {
                String sanitizedBody = htmlSanitizeUtils.sanitizeJsonString(originalBody);
                byte[] sanitizedBytes = sanitizedBody == null ? new byte[0] : sanitizedBody.getBytes(StandardCharsets.UTF_8);
                requestToUse = new CachedBodyAndParameterRequestWrapper(request, sanitizedBytes, sanitizedParameterMap);
            } catch (IOException ex) {
                // JSON 파싱/정제 실패 시 경고 로그를 남기고 원본 body로 진행
                log.warn("JSON sanitize failed, forwarding original request. uri={}, cause={}", request.getRequestURI(), ex.getMessage());
                requestToUse = new CachedBodyAndParameterRequestWrapper(request, rawBodyBytes, sanitizedParameterMap);
            }
        } else {
            // 비 JSON 요청은 파라미터 맵만 교체한 래퍼로 필터 체인을 진행
            requestToUse = new ParameterOnlyRequestWrapper(request, sanitizedParameterMap);
        }

        filterChain.doFilter(requestToUse, response);
    }


    /**
     * 요청 파라미터 맵을 복사하여 모든 문자열 값을 sanitizeString으로 정제한 새 맵을 반환합니다.
     * - 파라미터 값은 배열일 수 있으므로 각 요소를 개별적으로 정제합니다.
     */
    private Map<String, String[]> sanitizeParameterMap(HttpServletRequest request) {
        Map<String, String[]> sanitizedParameterMap = new HashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String[] sanitizedValues = Arrays.stream(entry.getValue())
                    .map(htmlSanitizeUtils::sanitizeString)
                    .toArray(String[]::new);
            sanitizedParameterMap.put(entry.getKey(), sanitizedValues);
        }
        return sanitizedParameterMap;
    }

    private static class DelegatingServletInputStream extends ServletInputStream {

        private final InputStream sourceStream;

        DelegatingServletInputStream(InputStream sourceStream) {
            this.sourceStream = sourceStream;
        }

        @Override
        public int read() throws IOException {
            return sourceStream.read();
        }

        @Override
        public boolean isFinished() {
            try {
                return sourceStream.available() == 0;
            } catch (IOException exception) {
                return true;
            }
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException("setReadListener is not supported");
        }
    }

    private static class ParameterOnlyRequestWrapper extends HttpServletRequestWrapper {

        private final Map<String, String[]> sanitizedParameterMap;

        ParameterOnlyRequestWrapper(HttpServletRequest request, Map<String, String[]> sanitizedParameterMap) {
            super(request);
            this.sanitizedParameterMap = sanitizedParameterMap;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return Collections.unmodifiableMap(sanitizedParameterMap);
        }

        @Override
        public String getParameter(String name) {
            String[] values = sanitizedParameterMap.get(name);
            return values != null && values.length > 0 ? values[0] : null;
        }

        @Override
        public String[] getParameterValues(String name) {
            return sanitizedParameterMap.get(name);
        }
    }

    private static class CachedBodyAndParameterRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] cachedBody;
        private final Map<String, String[]> sanitizedParameterMap;

        CachedBodyAndParameterRequestWrapper(HttpServletRequest request, byte[] cachedBody, Map<String, String[]> sanitizedParameterMap) {
            super(request);
            this.cachedBody = cachedBody;
            this.sanitizedParameterMap = sanitizedParameterMap;
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(cachedBody);
            return new DelegatingServletInputStream(inputStream);
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return Collections.unmodifiableMap(sanitizedParameterMap);
        }

        @Override
        public String getParameter(String name) {
            String[] values = sanitizedParameterMap.get(name);
            return values != null && values.length > 0 ? values[0] : null;
        }

        @Override
        public String[] getParameterValues(String name) {
            return sanitizedParameterMap.get(name);
        }
    }

}