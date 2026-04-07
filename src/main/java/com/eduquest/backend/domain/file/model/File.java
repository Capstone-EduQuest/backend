package com.eduquest.backend.domain.file.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    private Long id;
    private UUID uuid;
    private String storageType;
    private String originName;
    private String storedName;
    private LocalDateTime createdAt;

    @Builder(access = AccessLevel.PROTECTED)
    public File(String storageType, String originName, String storedName) {
        this.storageType = storageType;
        this.originName = originName;
        this.storedName = storedName;
    }

    public static File of(String storageType, String originName, String storedName) {
        return File.builder()
                .storageType(storageType)
                .originName(originName)
                .storedName(storedName)
                .build();
    }

}
