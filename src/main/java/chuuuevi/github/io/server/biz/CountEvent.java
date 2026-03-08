package chuuuevi.github.io.server.biz;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CountEvent {

    private boolean write;
    private long number;
    private BiConsumer<Long, Throwable> resultCallback;

    public CountEvent() {
        this.write = false;
        this.number = 0;
        this.resultCallback = null;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public BiConsumer<Long, Throwable> getResultCallback() {
        return resultCallback;
    }

    public void setResultCallback(BiConsumer<Long, Throwable> resultCallback) {
        this.resultCallback = resultCallback;
    }

    public void reset() {
        this.number = 0;
        this.write = false;
        this.resultCallback = null;
    }
}
