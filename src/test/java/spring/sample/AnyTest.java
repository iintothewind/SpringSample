package spring.sample;

import io.vavr.concurrent.Future;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class AnyTest {

    @Test
    public void testCompletionService1() {
        CompletableFuture
            .allOf(
                IntStream.range(1, 10)
                    .mapToObj(i -> String.format("number: %s", i))
                    .map(s -> CompletableFuture.runAsync(() -> log.info(s)))
                    .toArray(CompletableFuture[]::new)
            )
            .whenComplete(
                (v, t) -> {
                    if (t != null) {
                        log.error("completed with error", t);
                    } else {
                        log.info("####### completed successfully");
                    }
                }
            );
    }
}
