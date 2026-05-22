package com.eduquest.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {

    // 가상 스레드 기반 TaskExecutor 빈 등록
    @Bean(name = "virtualThreadTaskExecutor")
    public TaskExecutor virtualThreadTaskExecutor() { return new VirtualThreadTaskExecutor();}

}
