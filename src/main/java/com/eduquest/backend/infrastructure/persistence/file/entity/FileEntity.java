package com.eduquest.backend.infrastructure.persistence.file.entity;

import com.eduquest.backend.infrastructure.persistence.common.entity.BasicEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file")
public class FileEntity extends BasicEntity {

    private String storageType;
    private String originName;
    private String storedName;

    @Builder(access = AccessLevel.PROTECTED)
    public FileEntity(String storageType, String originName, String storedName) {
        this.storageType = storageType;
        this.originName = originName;
        this.storedName = storedName;
    }

    public static FileEntity of(String storageType, String originName, String storedName) {
        return FileEntity.builder()
                .storageType(storageType)
                .originName(originName)
                .storedName(storedName)
                .build();
    }

}
