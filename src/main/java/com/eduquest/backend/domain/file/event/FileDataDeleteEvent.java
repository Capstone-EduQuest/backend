package com.eduquest.backend.domain.file.event;

public record FileDataDeleteEvent(
        Long fileId
) {

    public static FileDataDeleteEvent of(Long fileId) {
        return new FileDataDeleteEvent(fileId);
    }

}
