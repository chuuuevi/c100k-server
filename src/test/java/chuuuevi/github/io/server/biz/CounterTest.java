package chuuuevi.github.io.server.biz;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

class CounterTest {

    private static final Logger log = LoggerFactory.getLogger(CounterTest.class);

    @Test
    public void testDelta() throws ExecutionException, InterruptedException {
        Counter counter = new Counter();

        counter.start();

        final int total = 15_0000;
        int iter = total;

        Instant begin = Instant.now();
        while (iter-- > 0) {
            counter.delta(1)
                    .get();
        }
        Instant end = Instant.now();

        Duration cost = Duration.between(begin, end);

        counter.close();

        log.info("counter total={}, cost={} ", total, cost);
    }
}