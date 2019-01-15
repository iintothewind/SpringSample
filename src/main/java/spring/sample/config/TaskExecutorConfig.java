package spring.sample.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;


@EnableAsync
@EnableRetry
@Configuration
public class TaskExecutorConfig implements AsyncConfigurer {
    @Autowired
    private ExecutorService forkJoinPool;

    @Bean(name = "forkJoinPool")
    public ExecutorService loadForkJoinPool() {
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, false);
    }

    @Bean(name = "listeningExecutorService")
    public ListeningExecutorService loadListeningExecutorService() {
        return MoreExecutors.listeningDecorator(forkJoinPool);
    }

    @Override
    public Executor getAsyncExecutor() {
        return forkJoinPool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(final Throwable throwable, final Method method,
                final Object... params) {
                throwable.printStackTrace();
            }
        };
    }
}
