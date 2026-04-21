package com.eduquest.backend.presentation.community.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateQuestionRequest(
        @NotBlank(message = "제목은 필수입니다.")
        String title,
        @NotBlank(message = "내용은 필수입니다.")
        String content
) {

    public static CreateQuestionRequest of(
            @NotBlank(message = "제목은 필수입니다.") String title,
            @NotBlank(message = "내용은 필수입니다.") String content
    ) {
        return new CreateQuestionRequest(title, content);
    }

}

