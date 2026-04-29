package com.eduquest.backend.infrastructure.mail.service;

import com.eduquest.backend.infrastructure.mail.repository.EmailTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.config.location=file:src/main/resources/application.yml,file:src/main/resources/application-dev.yml",
        "spring.main.allow-bean-definition-overriding=true" // <- 추가: 테스트에서 빈 덮어쓰기 허용
})
class AuthMailServiceRealSmtpTest {

    @Autowired
    private AuthMailService authMailService;

    @Autowired
    private EmailTokenRepository emailTokenRepository;

    @Test
    void sendSignUpMail_realSmtp_sendsEmailAndStoresToken() {

        // given
        String recipient = "test@test.com"; // 테스트 시 변경

        // when
        authMailService.sendSignUpMail(recipient);
        boolean exists = emailTokenRepository.existsByEmail(recipient);

        //then
        assertThat(exists).isTrue();

    }

    @TestConfiguration
    static class TestConfig {
        @Bean(name = "mailEventTaskExecutor")
        public TaskExecutor mailEventTaskExecutor() {
            // @Async("mailEventTaskExecutor")를 동기 실행으로 대체하여 테스트에서 즉시 수행되게 함
            return new SyncTaskExecutor();
        }
    }

}