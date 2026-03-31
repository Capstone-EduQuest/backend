package com.eduquest.backend.application.identity.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record ProfileCommand(
        String id,
        String email,
        String password,
        LocalDate birth,
        String nickname,
        MultipartFile profileImage
) {
}
