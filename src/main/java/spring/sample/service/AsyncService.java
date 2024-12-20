package spring.sample.service;

import io.vavr.control.Try;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncService {

    private final ExecutorService executorService;

    public static HttpServletRequest loadReq() {
        final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

    @Async
    @Retryable(retryFor = {IllegalArgumentException.class})
    public CompletableFuture<Integer> execute(Integer i) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("execute {} start", i);
            if (i > 5) {
                throw new IllegalArgumentException(String.format("%s is not acceptable", i));
            }
//            Try.run(() -> TimeUnit.SECONDS.sleep(1L));
            return i * 2;
        });
    }

    @Retryable(retryFor = {RuntimeException.class})
    public String echo(final String msg) {
        log.info("echo start");
        if (Strings.isEmpty(msg)) {
            throw new IllegalArgumentException("empty string");
        }
        return msg;
    }

    @Async
    public void specReq() {
        log.info(loadReq().getRequestURI());
        throw new IllegalArgumentException("try AsyncUncaughtExceptionHandler");
    }
}
