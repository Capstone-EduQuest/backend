package com.eduquest.backend.application.identity.service;

import com.eduquest.backend.application.identity.dto.UserProfileCommand;
import com.eduquest.backend.domain.file.component.CustomS3Client;
import com.eduquest.backend.domain.file.service.FileQueryService;
import com.eduquest.backend.domain.member.dto.MemberQuery;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileService {

    private final MemberQueryService memberQueryService;
    private final FileQueryService fileQueryService;
    private final CustomS3Client customS3Client;

    public UUID getUuidByUserId(String userId) {
        return memberQueryService.findUuidByUserId(userId);
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
                customS3Client.getPresignedUrl(storedName).orElse(null)
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

}
