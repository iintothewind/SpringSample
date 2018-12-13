package spring.sample.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class AsyncService {
    @Async
    public ListenableFuture<Integer> execute(Integer i) {
        if (i > 5) {
            throw new IllegalArgumentException(String.format("%s is not acceptable", i));
        }
        return new AsyncResult<>(i * 2);
    }
}
