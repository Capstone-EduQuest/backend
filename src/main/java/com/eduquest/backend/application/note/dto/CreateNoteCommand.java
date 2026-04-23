package com.eduquest.backend.application.note.dto;

public record CreateNoteCommand(
        String title,
        String content
) {
    public static CreateNoteCommand of(String title, String content) {
        return new CreateNoteCommand(title, content);
    }
}

