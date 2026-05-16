package com.eduquest.backend.application.identity.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record SignUpCommand(
        String id,
        String email,
        String password,
        String passwordValid,
        LocalDate birth,
        String nickname,
        MultipartFile profileImage
) {


}
