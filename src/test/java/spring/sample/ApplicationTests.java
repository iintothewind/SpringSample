package spring.sample;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.sample.service.AsyncService;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ApplicationTests {
    @Autowired
    private AsyncService asyncService;

    @After
    public void tearDown() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void test() {
        IntStream.range(1, 10).forEach(i -> {
            asyncService.execute(i);
        });
    }
}
