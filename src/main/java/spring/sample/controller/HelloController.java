package spring.sample.controller;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.sample.service.AsyncService;

import java.util.concurrent.atomic.AtomicLong;


@Slf4j
@RestController
public class HelloController {

    private static final String template = "Hello, %s!";

    @Autowired
    private AsyncService asyncService;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format(template, name);
    }

    @RequestMapping("/executeAsync")
    public String executeAsync() {
        CompletableFuture
            .allOf(
                IntStream.range(1, 10)
                    .mapToObj(i ->
                        asyncService
                            .execute(i)
                            .whenComplete((n, t) -> {
                                if (Objects.nonNull(t)) {
                                    log.error(t.getMessage(), t);
                                } else {
                                    log.info("input: {} , result: {}", i, n);
                                }
                            })
                    )
                    .toArray(CompletableFuture[]::new)
            )
            .whenComplete(
                (v, t) -> {
                    if (t != null) {
                        log.error("completed with error", t);
                    } else {
                        log.info("result: {}", v);
                    }
                }
            );
        return "committed";
    }

}
