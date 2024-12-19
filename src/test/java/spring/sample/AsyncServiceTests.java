package spring.sample;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import spring.sample.config.ExecutorConfig;
import spring.sample.service.AsyncService;


@Slf4j
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {ExecutorConfig.class})
public class AsyncServiceTests {

    private AsyncService asyncService;

    @BeforeEach
    public void beforeEach() {
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(3);
    }

    /**
     * this test case will not work as expected, because asyncService is a mocked instance.
     */
    @Test
    @Disabled
    public void testCompletionService() {
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
        log.info("done");

    }

    /**
     * this test case will not work as expected, because ayncService is a mocked instance.
     */

    @Test
    @Disabled
    public void testRetry() {
        System.out.println(asyncService.echo("sssssssss"));
        System.out.println(asyncService.echo(""));
    }

    @Test
    @Disabled
    public void testSpecReq() {
        asyncService.specReq();
    }

}
