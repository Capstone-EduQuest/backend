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
    // 파일 이벤트는 I/O 작업이 많기 때문에 가상 스레드를 활용하여 효율적으로 처리할 수 있음
    @Bean(name = "fileEventTaskExecutor")
    public TaskExecutor fileEventTaskExecutor() {
        return new VirtualThreadTaskExecutor();
    }

    // 메일 이벤트도 I/O 작업이 많기 때문에 가상 스레드를 활용하여 효율적으로 처리할 수 있음
    @Bean(name = "mailEventTaskExecutor")
    public TaskExecutor mailEventTaskExecutor() {
        return new VirtualThreadTaskExecutor();
    }

    // evaluation 이벤트 전용 가상 스레드 TaskExecutor
    @Bean(name = "evaluationTaskExecutor")
    public TaskExecutor evaluationTaskExecutor() {
        return new VirtualThreadTaskExecutor();
    }

    // hint 이벤트 전용 가상 스레드 TaskExecutor
    @Bean(name = "hintHistoryTaskExecutor")
    public TaskExecutor hintHistoryTaskExecutor() {
        return new VirtualThreadTaskExecutor();
    }

}
