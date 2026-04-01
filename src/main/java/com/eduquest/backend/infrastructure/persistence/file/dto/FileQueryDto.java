package com.eduquest.backend.infrastructure.persistence.file.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FileQueryDto {

    public record FileBasicInfo(
        Long id,
        UUID uuid,
        String storedName
    ){

    }

}
