package com.eduquest.backend.infrastructure.security.filter;

import com.eduquest.backend.infrastructure.security.util.HtmlSanitizeUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class XssResponseFilter extends OncePerRequestFilter {

	private final HtmlSanitizeUtils htmlSanitizeUtils;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return uri.startsWith("/api/v1/auth"); // 제외할 URI
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
		CachedBodyResponseWrapper responseWrapper = new CachedBodyResponseWrapper(response);
		// 체인 실행 — 응답은 래퍼에 캡처됩니다.
		filterChain.doFilter(request, responseWrapper);

		// 캡처된 바이트를 가져옵니다.
		byte[] rawBytes = responseWrapper.getCapturedAsBytes();

		if (rawBytes.length == 0) {
			// 캡처된 내용이 없으면 원본 응답 스트림으로 아무것도 쓰지 않습니다.
			return;
		}

		String contentType = responseWrapper.getContentType();
		boolean isJsonResponse = contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE);

		byte[] outputBytes;
		try {
			String charset = responseWrapper.getCharacterEncoding() != null ? responseWrapper.getCharacterEncoding() : StandardCharsets.UTF_8.name();
			String original = new String(rawBytes, charset);

			if (isJsonResponse) {
				// JSON 내부의 문자열들까지 재귀적으로 정제
				String sanitized = htmlSanitizeUtils.sanitizeJsonString(original);
				outputBytes = sanitized == null ? new byte[0] : sanitized.getBytes(StandardCharsets.UTF_8);
			} else {
				// 그 외는 단순 문자열 정제
				String sanitized = htmlSanitizeUtils.sanitizeString(original);
				outputBytes = sanitized == null ? new byte[0] : sanitized.getBytes(StandardCharsets.UTF_8);
			}
		} catch (Exception ex) {
			// 정제 실패 시 원본 응답을 그대로 사용
			log.warn("Response sanitize failed, forwarding original response. uri={}, cause={}", request.getRequestURI(), ex.getMessage());
			outputBytes = rawBytes;
		}

		// 응답 헤더 조정 및 최종 바이트 전송
		response.setContentLength(outputBytes.length);
		response.getOutputStream().write(outputBytes);
	}

	/**
	 * 응답을 바이트로 캡처하는 간단한 래퍼입니다.
	 */
	private static class CachedBodyResponseWrapper extends HttpServletResponseWrapper {

		private final ByteArrayOutputStream capture;
		private ServletOutputStream output;
		private PrintWriter writer;

		CachedBodyResponseWrapper(HttpServletResponse response) {
			super(response);
			this.capture = new ByteArrayOutputStream(1024);
		}

		@Override
		public ServletOutputStream getOutputStream() {
			if (this.output == null) {
				this.output = new ServletOutputStream() {
					@Override
					public boolean isReady() {
						return true;
					}

					@Override
					public void setWriteListener(WriteListener writeListener) {
						// 비동기 쓰기 리스너는 지원하지 않습니다.
						throw new UnsupportedOperationException("WriteListener is not supported");
					}

					@Override
					public void write(int b) throws IOException {
						capture.write(b);
					}
				};
			}
			return this.output;
		}

		@Override
		public PrintWriter getWriter() {
			if (this.writer == null) {
				this.writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), StandardCharsets.UTF_8));
			}
			return this.writer;
		}

		byte[] getCapturedAsBytes() {
			// writer가 열려 있으면 플러시하여 모든 데이터가 capture에 쓰이도록 함
			if (this.writer != null) {
				this.writer.flush();
			}
			if (this.output != null) {
				try {
					this.output.flush();
				} catch (IOException ignored) {
				}
			}
			return this.capture.toByteArray();
		}
	}

}
