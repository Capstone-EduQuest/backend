package com.eduquest.backend.presentation.identity.controller;

import com.eduquest.backend.application.identity.dto.ProfileCommand;
import com.eduquest.backend.application.identity.service.SignUpService;
import com.eduquest.backend.presentation.identity.dto.request.ProfileRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final SignUpService signUpService;

    @PostMapping(
            value = "/sign-up",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<String> signUp(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @Valid @RequestPart(value = "profile")ProfileRequest profileRequest
            ) {

        signUpService.signUp(
                new ProfileCommand(
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

}
