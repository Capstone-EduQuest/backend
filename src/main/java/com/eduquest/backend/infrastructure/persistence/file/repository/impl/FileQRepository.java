package com.eduquest.backend.infrastructure.persistence.file.repository.impl;

import java.util.Optional;

// Querydsl Repository
public interface FileQRepository {

    Optional<Long> findIdByStoredName(String storedName);

    Optional<String> findStoredNameById(Long id);

}
