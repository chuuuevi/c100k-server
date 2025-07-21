package chuuuevi.github.io.server.disruptor;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Counter {
    protected final Disruptor<Action> disruptor;

    private long total;

    public Counter() {
        this.disruptor = new Disruptor<>(
                Action::new,
                (int) Math.pow(2, 20),
                new DisruptorThreadFactory("counter", true),
                ProducerType.MULTI,
                new YieldingWaitStrategy()
        );

        this.disruptor.handleEventsWith(this::doAction);
        this.disruptor.start();
    }

    private void doAction(Action e, long disruptorSequence, boolean endOfBatch) {
        if (e.isWrite()) {
            this.total += e.getNumber();
        } else {
            if (e.getFuture() != null) {
                e.getFuture().complete(this.total);
            }
        }
        e.clear();
    }

    private void writeTranslator(Action action, long sequence, long val) {
        action.setNumber(val);
        action.setFuture(null);
        action.setWrite(true);
    }

    private void readTranslator(Action action, long sequence, CompletableFuture<Long> future) {
        action.setNumber(0);
        action.setFuture(future);
        action.setWrite(false);
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
