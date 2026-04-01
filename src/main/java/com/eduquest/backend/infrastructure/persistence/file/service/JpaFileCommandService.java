package com.eduquest.backend.infrastructure.persistence.file.service;

import com.eduquest.backend.domain.file.model.File;
import com.eduquest.backend.domain.file.service.FileCommandService;
import com.eduquest.backend.infrastructure.persistence.file.entity.FileEntity;
import com.eduquest.backend.infrastructure.persistence.file.mapper.FileMapper;
import com.eduquest.backend.infrastructure.persistence.file.repository.FileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaFileCommandService implements FileCommandService {

    private final FileJpaRepository fileJpaRepository;
    private final FileMapper fileMapper;

    @Override
    public Long saveFile(File file) {

        FileEntity fileEntity = fileMapper.toEntity(file);

        return fileJpaRepository.save(fileEntity).getId();

    }

    @Override
    public void deleteFile(Long fileId) {
        fileJpaRepository.deleteById(fileId);
    }
}
