package com.eduquest.backend.infrastructure.security.encoder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.config.location=file:src/main/resources/application.yml,file:src/main/resources/application-dev.yml")
@ActiveProfiles("dev")
class SecurityPasswordEncoderTest {

    @Autowired
    SecurityPasswordEncoder securityPasswordEncoder;

    @Test
    void encode() {

        // given
        String password = "Test1234@";

        // when
        String encodedPassword = securityPasswordEncoder.encode(password);

        // then
        assertTrue(securityPasswordEncoder.matches(password, encodedPassword));

    }
}