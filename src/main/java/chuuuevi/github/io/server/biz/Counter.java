package chuuuevi.github.io.server.biz;

import chuuuevi.github.io.server.thread.CpuAffinityThreadFactory;
import chuuuevi.github.io.server.thread.ResultDeffer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class Counter implements AutoCloseable {
    protected final Disruptor<CountEvent> disruptor;
    private final ResultDeffer<Long> resultDeffer;

    private long total;

    public Counter() {
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
        this.disruptor.start();
        this.resultDeffer.start();
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
        this.disruptor.shutdown();
        this.resultDeffer.close();
    }
}
