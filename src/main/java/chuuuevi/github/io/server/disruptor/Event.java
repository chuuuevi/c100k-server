package chuuuevi.github.io.server.disruptor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Event {

    private boolean write;
    private long number;

    private CompletableFuture<Long> future;

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


    public CompletableFuture<Long> getFuture() {
        return future;
    }

    public void setFuture(CompletableFuture<Long> future) {
        this.future = future;
    }

    public void clear() {
        this.number = 0;
        this.write = false;
        this.future = null;
    }
}
