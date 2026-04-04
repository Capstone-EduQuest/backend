package com.eduquest.backend.application.file.listener;

import com.eduquest.backend.domain.file.component.CustomS3Client;
import com.eduquest.backend.domain.file.event.FileDataDeleteEvent;
import com.eduquest.backend.domain.file.event.S3FileDeleteEvent;
import com.eduquest.backend.domain.file.service.FileCommandService;
import com.eduquest.backend.domain.file.service.FileQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileEventListener {

    private final CustomS3Client s3Client;
    private final FileCommandService fileCommandService;
    private final FileQueryService fileQueryService;

    // DB 파일 정보 삭제 이벤트를 처리하는 리스너 메서드. 실제로는 DB에서 파일 정보를 삭제하는 로직이 들어감
    @Async("fileEventTaskExecutor")
    @TransactionalEventListener
    public void handleFileDataDeleteEvent(FileDataDeleteEvent event) {
        Long fileId = event.fileId();
        // DB에서 파일 정보 삭제
        fileCommandService.deleteFile(fileId);
        log.info("DB 파일 정보 삭제 성공: fileId={}", fileId);
    }

    // S3 파일 삭제 이벤트를 처리하는 리스너 메서드. 실제로는 S3에서 파일을 삭제하는 로직이 들어감
    @Async("fileEventTaskExecutor")
    @TransactionalEventListener
    public void handleS3FileDeleteEvent(S3FileDeleteEvent event) {

        try {
            // S3에서 파일 삭제
            s3Client.deleteObject(event.fileId());
            log.info("S3 파일 삭제 성공: {}", event.fileId());
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}", event.fileId(), e);
        }

    }

}
