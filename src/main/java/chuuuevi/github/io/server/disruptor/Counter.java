package chuuuevi.github.io.server.disruptor;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Counter {
    protected final Disruptor<Event> disruptor;

    private long total;

    public Counter() {
        this.disruptor = new Disruptor<>(
                Event::new,
                (int) Math.pow(2, 20),
                DaemonThreadFactory.INSTANCE,
                ProducerType.MULTI,
                new YieldingWaitStrategy()
        );

        this.disruptor.handleEventsWith(this::onEvent);
        this.disruptor.start();
    }

    private void onEvent(Event e, long disruptorSequence, boolean endOfBatch) {
        if (e.isWrite()) {
            this.total += e.getNumber();
        } else {
            if (e.getFuture() != null) {
                e.getFuture().complete(this.total);
            }
        }

        e.clear();
    }

    private void writeTranslator(Event event, long sequence, long val) {
        event.setNumber(val);
        event.setFuture(null);
        event.setWrite(true);
    }

    private void readTranslator(Event event, long sequence, CompletableFuture<Long> future) {
        event.setNumber(0);
        event.setFuture(future);
        event.setWrite(false);
    }

    public void dealt(final long val) {
        this.disruptor.publishEvent(this::writeTranslator, val);
    }

    public long read() throws ExecutionException, InterruptedException {
        CompletableFuture<Long> future = new CompletableFuture<>();
        this.disruptor.publishEvent(this::readTranslator, future);
        return future.get();
    }
}
