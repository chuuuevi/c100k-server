package chuuuevi.github.io.server.server;

import chuuuevi.github.io.server.disruptor.Counter;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;

public class VertxServer {

    private static final Logger log = LoggerFactory.getLogger(VertxServer.class);

    public VertxServer(int port, Counter counter) {
        Vertx.vertx().deployVerticle(
                new VertxInternalServer(port, counter),
                new DeploymentOptions()
                        .setThreadingModel(ThreadingModel.VIRTUAL_THREAD))
                ;
        while (true) {
            LockSupport.park();
        }
    }
}
