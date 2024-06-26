package com.example.concurrency.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean("customThreadPool")
    public Executor customThreadPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolTaskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolTaskExecutor.setPrestartAllCoreThreads(true);
        threadPoolTaskExecutor.setThreadNamePrefix("custom-thread-");
        threadPoolTaskExecutor.setPrestartAllCoreThreads(true);
        return threadPoolTaskExecutor;
    }
}
