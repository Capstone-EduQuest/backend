package com.eduquest.backend.domain.file.event;

public record S3FileDeleteEvent(
        String fileId
) {

    public static S3FileDeleteEvent of(String fileId) {
        return new S3FileDeleteEvent(fileId);
    }

}
