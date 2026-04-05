package chuuuevi.github.io.server.biz;

import chuuuevi.github.io.server.thread.CpuAffinityThreadFactory;
import chuuuevi.github.io.server.thread.ResultDeffer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class Counter implements AutoCloseable {
    private final static Logger LOGGER = LoggerFactory.getLogger(Counter.class);

    private final AtomicInteger startupCount;
    private final AtomicInteger downCount;
    private final Disruptor<CountEvent> disruptor;
    private final ResultDeffer<Long> resultDeffer;

    private long total;

    public Counter() {
        this.startupCount = new AtomicInteger(0);
        this.downCount = new AtomicInteger(0);
        this.disruptor = new Disruptor<>(
                CountEvent::new,
                (int) Math.pow(2, 20),
                new CpuAffinityThreadFactory("counter-", true),
                ProducerType.MULTI,
                new BlockingWaitStrategy()
        );

        this.disruptor.handleEventsWith(this::handleEvent);

        this.resultDeffer = new ResultDeffer<>("result-");
    }

    public void start() {
        if (this.startupCount.incrementAndGet() == 1) {
            LOGGER.info("Starting counter");
            this.disruptor.start();
            this.resultDeffer.start();
        }
    }

    private void handleEvent(CountEvent e, long _1, boolean _2) {
        if (e.isWrite()) {
            this.total += e.getNumber();
        }

        final BiConsumer<Long, Throwable> callback = e.getResultCallback();
        e.reset();

        this.resultDeffer.done(
                callback,
                this.total,
                null);
    }

    public CompletableFuture<Long> delta(final long value) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        this.disruptor.publishEvent((event, _1) -> {
            event.setNumber(value);
            event.setWrite(true);
            event.setResultCallback((result, error) -> {
                if (error != null) {
                    future.completeExceptionally(error);
                } else {
                    future.complete(result);
                }
            });
        });
        return future;
    }

    public CompletableFuture<Long> read() {
        CompletableFuture<Long> future = new CompletableFuture<>();
        this.disruptor.publishEvent((event, _1) -> {
            event.setWrite(false);
            event.setResultCallback((result, error) -> {
                if (error != null) {
                    future.completeExceptionally(error);
                } else {
                    future.complete(result);
                }
            });
        });
        return future;
    }

    @Override
    public void close() {
        if (this.downCount.incrementAndGet() == 1) {
            LOGGER.info("Stopping counter");
            this.disruptor.shutdown();
            this.resultDeffer.close();
        }
    }
}
