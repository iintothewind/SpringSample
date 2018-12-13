package spring.sample;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.extern.slf4j.Slf4j;
import spring.sample.service.AsyncService;


@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ApplicationTests {
    @Autowired
    private AsyncService asyncService;

    @Autowired
    private ExecutorService forkJoinPool;

    @Autowired
    private ListeningExecutorService listeningExecutorService;

    @After
    public void tearDown() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void test() {
        IntStream.range(1, 10).forEach(i -> {
            ListenableFuture<Integer> future = asyncService.execute(i);
            //            future.completable().whenComplete((n, t) -> {
            //                if (n != null) {
            //                    System.out.println(n);
            //                } else {
            //                    t.printStackTrace();
            //                }
            //            });
            future.addCallback(System.out::println, Throwable::printStackTrace);
        });
    }

    @Test
    public void testCompletionService() {
        CompletableFuture
            .allOf(
                IntStream.range(1, 10)
                    .mapToObj(i ->
                        asyncService.execute(i).completable()
                            .whenComplete((n, t) -> {
                                if (n != null) {
                                    log.info("result: {}", n);
                                } else {
                                    log.error(t.getMessage(), t);
                                }
                            })
                    ).toArray(CompletableFuture[]::new)
            )
            .whenComplete(
                (v, t) -> {
                    if (t != null) {
                        log.error("completed with error", t);
                    } else {
                        log.info("completed successfully");
                    }
                }
            );
        log.info("done");
    }

}
