package com.eduquest.backend.application.identity.service;

import com.eduquest.backend.application.identity.component.ProfileImageFilter;
import com.eduquest.backend.application.identity.dto.UserProfileCommand;
import com.eduquest.backend.application.identity.exception.AuthErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.file.component.CustomS3Client;
import com.eduquest.backend.domain.file.dto.S3FileDto;
import com.eduquest.backend.domain.file.event.FileDataDeleteEvent;
import com.eduquest.backend.domain.file.event.S3FileDeleteEvent;
import com.eduquest.backend.domain.file.model.File;
import com.eduquest.backend.domain.file.model.StorageType;
import com.eduquest.backend.domain.file.service.FileCommandService;
import com.eduquest.backend.domain.file.service.FileQueryService;
import com.eduquest.backend.domain.identity.component.CustomPasswordEncoder;
import com.eduquest.backend.domain.identity.dto.MemberQuery;
import com.eduquest.backend.domain.identity.model.Member;
import com.eduquest.backend.domain.identity.service.MemberCommandService;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileService {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final FileCommandService fileCommandService;
    private final FileQueryService fileQueryService;
    private final CustomS3Client customS3Client;
    private final ApplicationEventPublisher eventPublisher;
    private final ProfileImageFilter profileImageFilter;
    private final CustomPasswordEncoder passwordEncoder;

    public UUID getUuidByUserId(String userId) {
        return memberQueryService.findMemberUuidByUserId(userId);
    }

    public UserProfileCommand.ProfileResponse getUserProfile(UUID uuid) {

        MemberQuery.UserProfile userProfile = memberQueryService.findUserProfileByUuid(uuid);

        String storedName = fileQueryService.findStoredNameByFileId(userProfile.profileId());

        return UserProfileCommand.ProfileResponse.of(
                userProfile.uuid(),
                userProfile.userId(),
                userProfile.birth(),
                userProfile.nickname(),
                userProfile.point(),
                userProfile.role(),
                userProfile.isLocked(),
                storedName != null ? customS3Client.getPresignedUrl(storedName).orElse(null) : null
        );
    }

    public List<UserProfileCommand.resultResponse> getUserProfileList(UserProfileCommand.UserListRequest request) {

        List<MemberQuery.UserListResult> userList = memberQueryService.findAllMembersByPagination(
                request.page(),
                request.size(),
                request.sort(),
                request.isAsc() ? "asc" : "desc"
        );

        return userList.stream()
                .map(user -> UserProfileCommand.resultResponse.of(
                        user.uuid(),
                        user.userId(),
                        user.email(),
                        user.nickname()
                ))
                .toList();
    }

    @Transactional
    public void changeProfile(UserProfileCommand.ProfileChangeRequest request) {

        Long fileId = request.profileImage() != null ? handleProfileImage(request) : null;

        Member member = memberQueryService.findMemberByEmail(request.email());

        member.updateProfile(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname()
        );

        // 새 프로필 이미지 존재하면 변경
        if (request.profileImage() != null) {

            // 기존 이미지 삭제
            String storedName = fileQueryService.findStoredNameByFileId(member.getProfileId());
            eventPublisher.publishEvent(S3FileDeleteEvent.of(storedName));
            eventPublisher.publishEvent(FileDataDeleteEvent.of(member.getProfileId()));

            member.updateProfileId(fileId);
        }

        // 회원 저장 시 예외가 발생하면 업로드된 파일을 삭제
        try {
            memberCommandService.updateMember(member);
        } catch (Exception e) {

            log.error("회원 저장 실패: {}", request.email());

            if (request.profileImage() != null) {
                String newStoredName = fileQueryService.findStoredNameByFileId(fileId);
                eventPublisher.publishEvent(S3FileDeleteEvent.of(newStoredName));
                eventPublisher.publishEvent(FileDataDeleteEvent.of(fileId));
            }

            throw e;
        }

    }

    // 탈퇴
    public void deleteMember(UUID uuid) {
        memberCommandService.deleteMember(uuid);
    }

    // 프로필 이미지 처리
    private Long handleProfileImage(UserProfileCommand.ProfileChangeRequest command) {
        String fileName = UUID.randomUUID().toString(); // 고유한 파일 이름 생성

        try (InputStream is = command.profileImage().getInputStream()) {

            if (!profileImageFilter.isImage(command.profileImage())) {
                throw new EduQuestException(AuthErrorCode.INVALID_PROFILE_FILE_FORMAT);
            }

            String extension = command.profileImage().getOriginalFilename().substring(command.profileImage().getOriginalFilename().lastIndexOf("."));

            customS3Client.putObject(
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
