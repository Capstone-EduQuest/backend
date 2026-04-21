package com.eduquest.backend.presentation.community.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAnswerRequest(
        @NotBlank(message = "내용은 필수입니다.")
        String content
) {

    public static CreateAnswerRequest of(@NotBlank(message = "내용은 필수입니다.") String content) {
        return new CreateAnswerRequest(content);
    }

}

