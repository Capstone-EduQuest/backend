package com.eduquest.backend.domain.file.component;

import com.eduquest.backend.infrastructure.s3.dto.S3FileDto;

import java.util.Optional;

public interface CustomS3Client {

    String putObject(String id, S3FileDto file);

    void deleteObject(String id);

    Optional<String> getPresignedUrl(String id);


}
