package com.eduquest.backend.domain.file.dto;

public record S3FileDto(
        String fileName,
        String fileType,
        byte[] fileData
) {

    public static S3FileDto of(
            String fileName,
            String fileType,
            byte[] fileData
    ) {
        return new S3FileDto(fileName, fileType, fileData);
    }

}
