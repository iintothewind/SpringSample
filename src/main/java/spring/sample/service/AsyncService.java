package spring.sample.service;

import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class AsyncService {
    public static HttpServletRequest loadReq() {
        final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

    @Async
    @Retryable(include = { RuntimeException.class })
    public ListenableFuture<Integer> execute(Integer i) {
        log.info("execute {} start", i);
        if (i > 5) {
            throw new IllegalArgumentException(String.format("%s is not acceptable", i));
        }
        return new AsyncResult<>(i * 2);
    }

    @Retryable(include = { RuntimeException.class })
    public String echo(final String msg) {
        log.info("echo start");
        if (Strings.isEmpty(msg)) {
            throw new IllegalArgumentException("empty string");
        }
        return msg;
    }

    public void specReq() {
        log.info(loadReq().getRequestURI());
    }
}
