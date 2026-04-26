package com.eduquest.backend.infrastructure.ai.service;

import com.eduquest.backend.domain.submission.dto.AiFeedBackRequest;
import com.eduquest.backend.domain.submission.service.ChatModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiChatModelService implements ChatModelService {

    private final ChatClient chatClient;

    @Override
    public String generateAiExplanation(AiFeedBackRequest request) {

        String systemMessageText = "당신은 초등학생에게 파이썬을 가르치는 친절한 튜터입니다. " +
                "모든 설명은 쉬운 한국어로, 비난 없이 격려하는 어투로 작성하세요. " +
                "출력은 aiExplanation(간단요약)용 텍스트를 우선 제공합니다.";

        SystemMessage systemMessage = new SystemMessage(systemMessageText);

        UserMessage userMessage = new UserMessage(buildPrompt(request));

        Prompt prompt = Prompt.builder()
                .messages(List.of(systemMessage, userMessage))
                .build();

        return chatClient.prompt(prompt).call().content();

    }

    private String buildPrompt(AiFeedBackRequest request) {

        StringBuilder sb = new StringBuilder();
        sb.append("서비스 컨텍스트: 이 서비스는 초등학생 대상의 파이썬 교육용 유니티 게임입니다.\n");
        sb.append("문제: ").append(request.problemText()).append("\n");
        sb.append("정답(참고): ").append(request.correctAnswer()).append("\n");
        sb.append("학생의 답: ").append(request.userAnswer()).append("\n\n");

        sb.append("요청: 아래 규칙에 따라 문자열 형태로 응답해 주세요.\n");
        sb.append("(요약: 간결한 한-세 문장 요약, 설명: 전체 피드백 등). ");
        sb.append("어조는 친절하고 쉬운 한국어로 작성하세요.\n");

        if (request.metadata() != null && !request.metadata().isEmpty()) {
            sb.append("메타데이터: ").append(request.metadata().toString()).append("\n");
        }

        return sb.toString();

    }

}
