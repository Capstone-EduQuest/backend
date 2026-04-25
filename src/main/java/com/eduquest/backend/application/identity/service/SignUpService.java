package com.eduquest.backend.application.identity.service;

import com.eduquest.backend.application.identity.component.ProfileImageFilter;
import com.eduquest.backend.application.identity.dto.SignUpCommand;
import com.eduquest.backend.application.identity.exception.AuthErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.file.dto.S3FileDto;
import com.eduquest.backend.domain.file.event.FileDataDeleteEvent;
import com.eduquest.backend.domain.file.event.S3FileDeleteEvent;
import com.eduquest.backend.domain.file.model.File;
import com.eduquest.backend.domain.file.model.StorageType;
import com.eduquest.backend.domain.file.service.FileCommandService;
import com.eduquest.backend.domain.file.service.FileQueryService;
import com.eduquest.backend.domain.identity.component.CustomPasswordEncoder;
import com.eduquest.backend.domain.identity.model.Member;
import com.eduquest.backend.domain.identity.model.enums.RoleType;
import com.eduquest.backend.domain.identity.service.MemberCommandService;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.domain.reward.event.GrantPointEvent;
import com.eduquest.backend.domain.identity.event.SignUpMailEvent;
import com.eduquest.backend.domain.reward.service.WalletCommandService;
import com.eduquest.backend.infrastructure.s3.client.EduQuestS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ApplicationEventPublisher eventPublisher;
    private final ProfileImageFilter profileImageFilter;
    private final MemberQueryService memberQueryService;
    private final WalletCommandService walletCommandService;

    // 회원 가입 처리
    @Transactional
    public void signUp(SignUpCommand command) {

        Long fileId = command.profileImage() != null ? handleProfileImage(command) : 0L;

        // 회원 정보 저장
        Member member = Member.of(
                command.id(),
                command.email(),
                passwordEncoder.encode(command.password()),
                command.birth(),
                command.nickname(),
                true,
                fileId
        );

        try {
            memberCommandService.saveMember(member, RoleType.USER.toString());

            // 저장된 멤버의 PK(memberId)를 조회
            UUID savedUuid = memberQueryService.findMemberUuidByUserId(member.getUserId());
            Long savedMemberId = memberQueryService.findMemberIdByUuid(savedUuid);

            // 회원의 wallet 초기화 (wallet이 없으면 생성하도록 WalletCommandService 이용)
            walletCommandService.changeBalance(savedMemberId, 0L, "init-wallet");

            // 회원 가입 성공 이벤트 발행(이벤트 기반) - 기본 포인트 지급
            eventPublisher.publishEvent(SignUpMailEvent.of(command.email()));
            eventPublisher.publishEvent(GrantPointEvent.of(savedMemberId, 1000L, "sign-up"));

        } catch (Exception e) {

            log.error("회원 저장 실패: {}", command.email());

            if (command.profileImage() != null) {
                String storedName = fileQueryService.findStoredNameByFileId(fileId);
                eventPublisher.publishEvent(S3FileDeleteEvent.of(storedName));
                eventPublisher.publishEvent(FileDataDeleteEvent.of(fileId));
            }

            throw e;
        }


    }

    // 프로필 이미지 처리
    private Long handleProfileImage(SignUpCommand command) {
        String fileName = UUID.randomUUID().toString(); // 고유한 파일 이름 생성

        try (InputStream is = command.profileImage().getInputStream()) {

            if (!profileImageFilter.isImage(command.profileImage())) {
                throw new EduQuestException(AuthErrorCode.INVALID_PROFILE_FILE_FORMAT);
            }

            String extension = command.profileImage().getOriginalFilename().substring(command.profileImage().getOriginalFilename().lastIndexOf("."));

            s3Client.putObject(
                    fileName,
                    S3FileDto.of(
                            command.profileImage().getOriginalFilename(),
                            command.profileImage().getContentType(),
                            extension,
                            is.readAllBytes()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("프로필 이미지 S3 업로드 성공: {}", fileName);

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
