package com.eduquest.backend.domain.file.service;

import com.eduquest.backend.domain.file.model.File;

public interface FileCommandService {

    Long saveFile(File file);

    void deleteFile(Long fileId);

}
