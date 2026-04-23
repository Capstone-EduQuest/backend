package com.eduquest.backend.presentation.note.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoteUpdateRequest(
        @NotBlank(message = "title은 필수입니다.")
        @Size(max = 255, message = "title은 255자 이하여야 합니다.")
        String title,

        @NotBlank(message = "content는 필수입니다.")
        String content
) {
}

