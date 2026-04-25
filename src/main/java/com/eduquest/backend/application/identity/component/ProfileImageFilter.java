package com.eduquest.backend.application.identity.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfileImageFilter {

    private static final List<String> ALLOWED_EXTENSIONS = List.of(
            ".jpg",
            ".jpeg",
            ".png",
            ".webp"
    );

    public boolean isImage(MultipartFile file) {

        if (file == null || file.getOriginalFilename() == null || file.getOriginalFilename().lastIndexOf(".") == -1) {
            return false;
        }

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        // extension이 이미지 파일인지 체크
        return ALLOWED_EXTENSIONS.contains(extension);

    }

}
