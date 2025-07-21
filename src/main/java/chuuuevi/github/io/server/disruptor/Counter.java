package chuuuevi.github.io.server.disruptor;

import chuuuevi.github.io.server.thread.CpuAffinityThreadFactory;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CompletableFuture;

public class Counter {
    protected final Disruptor<CountEvent> disruptor;

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
        this.disruptor.start();
    }

    private void handleEvent(CountEvent e, long _1, boolean _2) {
        if (e.isWrite()) {
            this.total += e.getNumber();
        } else {
            if (e.getFuture() != null) {
                e.getFuture().complete(this.total);
            }
        }
        e.clear();
    }

    private void writeTranslator(CountEvent countEvent, long sequence, long val) {
        countEvent.setNumber(val);
        countEvent.setFuture(null);
        countEvent.setWrite(true);
    }

    private void readTranslator(CountEvent countEvent, long sequence, CompletableFuture<Long> future) {
        countEvent.setNumber(0);
        countEvent.setFuture(future);
        countEvent.setWrite(false);
    }

    public void dealt(final long val) {
        this.disruptor.publishEvent(this::writeTranslator, val);
    }

    public CompletableFuture<Long> readAsync() {
        CompletableFuture<Long> future = new CompletableFuture<>();
        this.disruptor.publishEvent(this::readTranslator, future);
        return future;
    }
}
