package com.eduquest.backend.domain.file.service;

public interface FileQueryService {

    Long findFileIdByStoredName(String storedName);

    String findStoredNameByFileId(Long fileId);

}
