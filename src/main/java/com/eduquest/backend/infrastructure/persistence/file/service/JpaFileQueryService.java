package com.eduquest.backend.infrastructure.persistence.file.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.file.service.FileQueryService;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.file.repository.FileQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JpaFileQueryService implements FileQueryService {

    private final FileQueryRepository fileQueryRepository;

    @Override
    public Long findFileIdByStoredName(String storedName) {
        return fileQueryRepository.findIdByStoredName(storedName)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA,
                        new HashMap<>(){{
                            put("존재하지 않는 파일입니다.", storedName);
                        }}
                ));
    }

    @Override
    public String findStoredNameByFileId(Long fileId) {
        return fileQueryRepository.findStoredNameById(fileId)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA,
                        new HashMap<>(){{
                            put("존재하지 않는 파일입니다.", fileId);
                        }}
                ));
    }
}
