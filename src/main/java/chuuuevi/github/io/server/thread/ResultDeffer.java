package chuuuevi.github.io.server.thread;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.function.BiConsumer;

public class ResultDeffer<R> implements AutoCloseable {

    private final Disruptor<Event<R>> disruptor;

    public ResultDeffer(String name) {
        this(name, false);
    }

    public ResultDeffer(String name, boolean multiThread) {
        this.disruptor = new Disruptor<>(
                Event::new,
                (int) Math.pow(2, 16),
                new CpuAffinityThreadFactory(name, true),
                multiThread ? ProducerType.MULTI : ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );
        this.disruptor.handleEventsWith(this::handleEvent);
    }

    public void start() {
        this.disruptor.start();
    }

    @Override
    public void close() {
        this.disruptor.shutdown();
    }

    public void done(final BiConsumer<R, Throwable> callback, final R result, final Throwable error) {
        this.disruptor.publishEvent((event, _1) -> {
            event.setCallback(callback);
            event.setResult(result);
            event.setError(error);
        });
    }

    private void handleEvent(Event<R> event, long _1, boolean _2) {
        final BiConsumer<R, Throwable> callback = event.getCallback();
        final R result = event.getResult();
        final Throwable error = event.getError();
        event.reset();

        if (callback != null) {
            callback.accept(result, error);
        }
    }

    public static class Event<R> {
        private BiConsumer<R, Throwable> callback;
        private R result;
        private Throwable error;

        public Event() {
            this.callback = null;
            this.result = null;
            this.error = null;
        }

        public void reset() {
            this.callback = null;
            this.result = null;
            this.error = null;
        }

        public BiConsumer<R, Throwable> getCallback() {
            return callback;
        }

        public void setCallback(BiConsumer<R, Throwable> callback) {
            this.callback = callback;
        }

        public R getResult() {
            return result;
        }

        public void setResult(R result) {
            this.result = result;
        }

        public Throwable getError() {
            return error;
        }

        public void setError(Throwable error) {
            this.error = error;
        }
    }
}
