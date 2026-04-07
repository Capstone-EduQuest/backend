package com.eduquest.backend.infrastructure.persistence.file.repository;

import com.eduquest.backend.infrastructure.persistence.file.entity.FileEntity;
import com.eduquest.backend.infrastructure.persistence.file.repository.impl.FileQRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Querydsl + etc. Repository
@Repository
public interface FileQueryRepository extends FileQRepository, JpaRepository<FileEntity, Long> {

}
