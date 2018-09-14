package spring.sample.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class AsyncService {
    @Async
    public void execute(Integer i) {
        log.info("working on " + i);
    }
}
