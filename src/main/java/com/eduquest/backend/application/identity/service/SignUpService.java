package com.eduquest.backend.application.identity.service;

import com.eduquest.backend.application.identity.dto.ProfileCommand;
import com.eduquest.backend.domain.file.model.File;
import com.eduquest.backend.domain.file.model.StorageType;
import com.eduquest.backend.domain.file.service.FileCommandService;
import com.eduquest.backend.domain.file.service.FileQueryService;
import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.model.enums.RoleType;
import com.eduquest.backend.domain.member.service.MemberCommandQueryService;
import com.eduquest.backend.infrastructure.s3.client.EduQuestS3Client;
import com.eduquest.backend.infrastructure.s3.dto.S3FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final MemberCommandQueryService memberCommandQueryService;
    private final FileCommandService fileCommandService;
    private final FileQueryService fileQueryService;
    private final EduQuestS3Client s3Client;

    // 회원 가입 처리
    public void signUp(ProfileCommand command) {

        // 프로필 파일 업로드
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
        Long fileId = fileCommandService.saveFile(
                File.of(
                        StorageType.S3.toString(),
                        command.profileImage().getOriginalFilename(),
                        fileName
                )
        );

        // 회원 정보 저장
        Member member = Member.of(
                command.id(),
                command.email(),
                command.password(),
                command.birth(),
                command.nickname(),
                false,
                fileId
        );

        try {
            memberCommandQueryService.saveMember(member, RoleType.USER.toString());
        } catch (Exception e) {
            // 회원 저장 실패 시 업로드된 파일 삭제
            s3Client.deleteObject(fileName);
            fileCommandService.deleteFile(fileId);
            throw e; // 예외 재발생
        }

    }

}
