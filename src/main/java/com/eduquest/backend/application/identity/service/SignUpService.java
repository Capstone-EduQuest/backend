package com.eduquest.backend.application.identity.service;

import com.eduquest.backend.application.identity.dto.ProfileCommand;
import com.eduquest.backend.domain.file.event.FileDataDeleteEvent;
import com.eduquest.backend.domain.file.event.S3FileDeleteEvent;
import com.eduquest.backend.domain.file.model.File;
import com.eduquest.backend.domain.file.model.StorageType;
import com.eduquest.backend.domain.file.service.FileCommandService;
import com.eduquest.backend.domain.file.service.FileQueryService;
import com.eduquest.backend.domain.member.component.CustomPasswordEncoder;
import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.model.enums.RoleType;
import com.eduquest.backend.domain.member.service.MemberCommandService;
import com.eduquest.backend.infrastructure.s3.client.EduQuestS3Client;
import com.eduquest.backend.infrastructure.s3.dto.S3FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpService {

    private final MemberCommandService memberCommandService;
    private final FileCommandService fileCommandService;
    private final FileQueryService fileQueryService;
    private final EduQuestS3Client s3Client;
    private final CustomPasswordEncoder passwordEncoder;
    public final ApplicationEventPublisher eventPublisher;

    // 회원 가입 처리
    public void signUp(ProfileCommand command) {

        Long fileId = command.profileImage() != null ? handleProfileImage(command) : null;

        // 회원 정보 저장
        Member member = Member.of(
                command.id(),
                command.email(),
                passwordEncoder.encode(command.password()),
                command.birth(),
                command.nickname(),
                false,
                fileId
        );

        try {
            memberCommandService.saveMember(member, RoleType.USER.toString());
        } catch (Exception e) {

            log.error("회원 저장 실패: {}", command.email());

            if (command.profileImage() != null) {
                String storedName = fileQueryService.findStoredNameByFileId(fileId);
                eventPublisher.publishEvent(S3FileDeleteEvent.of(storedName));
                eventPublisher.publishEvent(FileDataDeleteEvent.of(fileId));
            }

            throw e;
        }

        // Todo : 회원 가입 성공 이벤트 발행(이벤트 기반) - 기본 포인트 지급

    }

    // 프로필 이미지 처리
    private Long handleProfileImage(ProfileCommand command) {
        String fileName = UUID.randomUUID().toString(); // 고유한 파일 이름 생성

        try (InputStream is = command.profileImage().getInputStream()) {
            s3Client.putObject(
                    fileName,
                    S3FileDto.of(
                            command.profileImage().getOriginalFilename(),
                            command.profileImage().getContentType(),
                            is.readAllBytes()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 업로드된 파일 정보를 DB에 저장
        return fileCommandService.saveFile(
                File.of(
                        StorageType.S3.toString(),
                        command.profileImage().getOriginalFilename(),
                        fileName
                )
        );
    }

}
