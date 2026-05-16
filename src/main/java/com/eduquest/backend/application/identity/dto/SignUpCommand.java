package com.eduquest.backend.application.identity.dto;

import com.eduquest.backend.application.identity.exception.IdentityErrorCode;
import com.eduquest.backend.common.exception.EduQuestException;
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

    public SignUpCommand {
        if (password.isBlank() || !password.equals(passwordValid)) {
            throw new EduQuestException(IdentityErrorCode.PASSWORD_VALID_NOT_SAME);
        }
    }

}
