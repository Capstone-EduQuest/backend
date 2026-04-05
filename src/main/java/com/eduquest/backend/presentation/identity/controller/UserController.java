package com.eduquest.backend.presentation.identity.controller;

import com.eduquest.backend.application.identity.dto.RoleCommand;
import com.eduquest.backend.application.identity.dto.SignUpCommand;
import com.eduquest.backend.application.identity.dto.UserProfileCommand;
import com.eduquest.backend.application.identity.service.AdminUserService;
import com.eduquest.backend.application.identity.service.RoleService;
import com.eduquest.backend.application.identity.service.SignUpService;
import com.eduquest.backend.application.identity.service.UserProfileService;
import com.eduquest.backend.presentation.identity.dto.request.ProfileRequest;
import com.eduquest.backend.presentation.identity.dto.request.ProfileUpdateRequest;
import com.eduquest.backend.presentation.identity.dto.request.RoleUpdateRequest;
import com.eduquest.backend.presentation.identity.dto.request.UserListRequest;
import com.eduquest.backend.presentation.identity.dto.response.RoleResponse;
import com.eduquest.backend.presentation.identity.dto.response.UserListResponse;
import com.eduquest.backend.presentation.identity.dto.response.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final SignUpService signUpService;
    private final UserProfileService userProfileService;
    private final RoleService roleService;
    private final AdminUserService adminUserService;

    @PostMapping(
            value = "/sign-up",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<String> signUp(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @Valid @RequestPart(value = "profile")ProfileRequest profileRequest
            ) {

        signUpService.signUp(
                new SignUpCommand(
                        profileRequest.id(),
                        profileRequest.email(),
                        profileRequest.password(),
                        profileRequest.birth(),
                        profileRequest.nickname(),
                        profileImage
                )
        );

        return ResponseEntity.status(201).body("회원가입 성공");
    }

    @GetMapping("/users/{userId}/uuid")
    public ResponseEntity<String> getUuidByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(userProfileService.getUuidByUserId(userId).toString());
    }

    @GetMapping("/users/{uuid}")
    public ResponseEntity<UserProfileResponse> getUser(@PathVariable UUID uuid) {

        UserProfileCommand.ProfileResponse profileResponse = userProfileService.getUserProfile(uuid);

        return ResponseEntity.ok(UserProfileResponse.of(
                profileResponse.uuid(),
                profileResponse.userId(),
                profileResponse.birth(),
                profileResponse.nickname(),
                profileResponse.point(),
                profileResponse.role(),
                profileResponse.isLocked(),
                profileResponse.profile()
        ));
    }

    @GetMapping("/users")
    public ResponseEntity<UserListResponse.UserList> getUserList(@Valid @ModelAttribute UserListRequest request) {
        return ResponseEntity.ok(UserListResponse.UserList.of(
                request.page(),
                request.size(),
                request.sort(),
                request.isAsc(),
                userProfileService.getUserProfileList(
                        UserProfileCommand.UserListRequest.of(
                                request.page(),
                                request.size(),
                                request.sort(),
                                request.isAsc()
                        )
                ).stream().map(profileResponse -> UserListResponse.result.of(
                        profileResponse.uuid(),
                        profileResponse.userId(),
                        profileResponse.email(),
                        profileResponse.nickname()
                )).toList()

        ));
    }

    @GetMapping("/users/roles")
    public ResponseEntity<List<RoleResponse>> getRoleList() {

        List<RoleResponse> results = roleService.getRoles().stream().map(role -> RoleResponse.of(
                role.uuid(),
                role.name()
        )).toList();

        return ResponseEntity.ok(results);
    }

    @PutMapping("/users/{uuid}")
    public ResponseEntity<String> updateProfile(
            @PathVariable UUID uuid,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @Valid @RequestPart(value = "profile")ProfileUpdateRequest request
            ) {

        userProfileService.changeProfile(
                UserProfileCommand.ProfileChangeRequest.of(
                        uuid,
                        request.email(),
                        request.password(),
                        request.nickname(),
                        profileImage
                )
        );

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{uuid}/role")
    public ResponseEntity<String> updateRole(
            @PathVariable UUID uuid,
            @Valid RoleUpdateRequest request
    ) {

        roleService.updateRole(RoleCommand.RoleUpdateRequest.of(
                uuid,
                request.uuid()
        ));

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{uuid}/lock")
    public ResponseEntity<String> lockUser(
            @PathVariable UUID uuid
    ) {

        adminUserService.lockMember(uuid);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{uuid}")
    public ResponseEntity<String> deleteUser(
            @PathVariable UUID uuid
    ) {

        userProfileService.deleteMember(uuid);

        return ResponseEntity.noContent().build();
    }

}
