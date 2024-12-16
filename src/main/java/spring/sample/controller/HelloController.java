package spring.sample.controller;

import io.vavr.concurrent.Future;
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
        final Future<Integer> reduced = Future.reduce(
            IntStream.range(1, 9)
                .mapToObj(i ->
                    Future.fromCompletableFuture(
                        asyncService
                            .execute(i)
                            .handle((n, t) -> {
                                if (Objects.nonNull(t)) {
                                    log.error("failed to execute async service: {}", t.getMessage());
                                    return 0;
                                } else {
                                    log.info("input: {} , result: {}", i, n);
                                    return n;
                                }
                            })
                    )
                )
                .toList(),
            (l, r) -> l + r
        );

        final String result = String.valueOf(reduced.getOrElse(-1));
        return result;
    }

}
