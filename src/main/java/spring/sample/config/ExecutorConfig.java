package spring.sample.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Slf4j
@EnableAsync
@EnableRetry
@Configuration
public class ExecutorConfig implements AsyncConfigurer, SchedulingConfigurer {

    private final ExecutorService executorService;

    public ExecutorConfig() {
        executorService = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean(name = "executorService")
    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public Executor getAsyncExecutor() {
        return executorService;
    }

    @Override
    public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(executorService);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            log.error("failed to execute async task, method: {}", method.getName(), throwable);
        };
    }


}
