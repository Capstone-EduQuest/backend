package com.eduquest.backend.infrastructure.ai.service;

import com.eduquest.backend.domain.submission.dto.AiFeedBackRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.config.location=file:src/main/resources/application.yml,file:src/main/resources/application-dev.yml")
@ActiveProfiles("dev")
class GeminiChatModelServiceTest {

    @Autowired
    GeminiChatModelService geminiChatModelService;

    @Test
    void generateAiExplanation() {

        // given
        AiFeedBackRequest request = AiFeedBackRequest.of(
                "두 수를 더하는 문제: a + b = ?",
                "정답: a + b",
                "학생의 답: a - b",
                Map.of("audience", "elementary-unity-python")
        );

        // when
        String aiExplanation = assertTimeoutPreemptively(Duration.ofSeconds(60),
                () -> geminiChatModelService.generateAiExplanation(request));

        // then
        System.out.println(aiExplanation);
        assertNotNull(aiExplanation, "AI 응답은 null이 아니어야 합니다.");
        assertFalse(aiExplanation.isBlank(), "AI 응답은 빈 문자열이면 안 됩니다.");

    }
}