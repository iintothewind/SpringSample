package spring.sample.controller;

import io.vavr.concurrent.Future;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.sample.model.RoutePlanJob;
import spring.sample.service.AsyncService;

import java.util.concurrent.atomic.AtomicLong;
import spring.sample.service.DbService;


@Slf4j
@RestController
@RequiredArgsConstructor
public class HelloController {

    private static final String template = "Hello, %s!";

    private final AsyncService asyncService;

    private final DbService dbService;

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


    @RequestMapping("/jobs")
    public ResponseEntity<List<RoutePlanJob>> listJobs() {
        final List<RoutePlanJob> jobs = dbService.peekJobs("optimized", 99);
        return ResponseEntity.ok(jobs);
    }

}
