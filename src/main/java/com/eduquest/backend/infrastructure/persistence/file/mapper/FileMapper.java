package com.eduquest.backend.infrastructure.persistence.file.mapper;

import com.eduquest.backend.domain.file.model.File;
import com.eduquest.backend.infrastructure.persistence.file.entity.FileEntity;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public FileEntity toEntity(File file) {
        if (file == null) {
            return null;
        }

        // Use the entity factory method to construct FileEntity
        return FileEntity.of(
                file.getStorageType(),
                file.getOriginName(),
                file.getStoredName()
        );
    }

    public File toDomain(FileEntity fileEntity) {
        if (fileEntity == null) {
            return null;
        }

        // Use domain factory 'of' to create File (excluding persistence metadata)
        return File.of(
                fileEntity.getStorageType(),
                fileEntity.getOriginName(),
                fileEntity.getStoredName()
        );
    }

}
