package chuuuevi.github.io.server.disruptor;


import chuuuevi.github.io.server.C100kServer;
import net.openhft.affinity.AffinityLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class DisruptorThreadFactory implements ThreadFactory {
    private static final Logger log = LoggerFactory.getLogger(DisruptorThreadFactory.class);

    private final String threadNamePrefix;
    private final AtomicLong count;

    private final boolean cpuAffinity;

    public DisruptorThreadFactory(String threadNamePrefix, final boolean cpuAffinity) {
        this.threadNamePrefix = threadNamePrefix;
        this.count = new AtomicLong(0);
        this.cpuAffinity = cpuAffinity;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("runnable is null!");
        }
        String name = this.threadNamePrefix + this.count.getAndIncrement();
        Thread thread = null;
        if (cpuAffinity) {
            thread = new Thread(null, () -> {
                try (AffinityLock al = AffinityLock.acquireCore()) {
                    log.error("newThread {} affinity lock bound={}, cpuId={}",
                            Thread.currentThread().getName(), al.isBound(), al.cpuId());

                    runnable.run();
                }
            }, name);
        } else {
            thread = new Thread(null, runnable, name);
        }

        thread.setDaemon(true);
        return thread;
    }
}
