package com.eduquest.backend.application.note.dto;

public record UpdateNoteCommand(
        String title,
        String content
) {
    public static UpdateNoteCommand of(String title, String content) {
        return new UpdateNoteCommand(title, content);
    }
}

