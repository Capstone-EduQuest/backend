package com.eduquest.backend.infrastructure.security.encoder;

import com.eduquest.backend.domain.identity.component.CustomPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityPasswordEncoder implements CustomPasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String plainText) {
        return passwordEncoder.encode(plainText);
    }
}
