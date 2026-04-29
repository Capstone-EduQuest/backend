package com.eduquest.backend.infrastructure.persistence.file.repository;

import com.eduquest.backend.infrastructure.persistence.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileJpaRepository extends JpaRepository<FileEntity, Long> {
}
