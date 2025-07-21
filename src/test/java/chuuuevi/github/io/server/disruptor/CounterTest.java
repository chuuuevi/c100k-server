package chuuuevi.github.io.server.disruptor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class CounterTest {

    private static final Logger log = LoggerFactory.getLogger(CounterTest.class);

    @Test
    public void testSingleProducer() throws ExecutionException, InterruptedException {
        Counter counter = new Counter();

        final int total = 15_0000;
        int iter = total;

        Instant begin = Instant.now();
        while (iter-- > 0) {
            counter.dealt(1);
            counter.readAsync()
                    .get();
        }
        Instant end = Instant.now();

        Duration cost = Duration.between(begin, end);

        log.info("counter total={}, cost={} ", total, cost);
    }

    @Test
    public void testVT() throws ExecutionException, InterruptedException {
        Counter counter = new Counter();

        final int total = 15_0000;
        int iter = total;

        Executor executor = Executors.newVirtualThreadPerTaskExecutor();

        Instant begin = Instant.now();
        while (iter-- > 0) {

            executor.execute(()->{
                counter.dealt(1);
                try {
                    counter.readAsync()
                            .get();
                } catch (Exception e) {

                }
            });
        }

        counter.readAsync()
                .get();

        Instant end = Instant.now();

        Duration cost = Duration.between(begin, end);

        log.info("counter total={}, cost={} ", total, cost);
    }


    @Test
    public void testVT2() throws ExecutionException, InterruptedException {
        Counter counter = new Counter();

        final int total = 15_0000;
        int iter = total;

        Executor executor = Executors.newVirtualThreadPerTaskExecutor();

        Instant begin = Instant.now();
        while (iter-- > 0) {

            executor.execute(()->{
                counter.dealt(1);
            });
        }

        counter.readAsync()
                .get();

        Instant end = Instant.now();

        Duration cost = Duration.between(begin, end);

        log.info("counter total={}, cost={} ", total, cost);
    }
}