package spring.sample.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@EnableRetry
@Configuration
public class TaskExecutorConfig implements AsyncConfigurer {

    private final ExecutorService forkJoinPool;

    public TaskExecutorConfig() {
        forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() - 1, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, false);
    }

    @Bean(name = "executorService")
    public ExecutorService loadForkJoinPool() {
        return forkJoinPool;
    }

    @Override
    public Executor getAsyncExecutor() {
        return forkJoinPool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            log.error("failed to execute async task:", throwable);
        };
    }
}
