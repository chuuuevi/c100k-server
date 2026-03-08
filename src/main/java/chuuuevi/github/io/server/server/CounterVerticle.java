package chuuuevi.github.io.server.server;

import chuuuevi.github.io.server.biz.Counter;
import chuuuevi.github.io.server.biz.LongToFixedWidthBytes;
import io.netty.util.AsciiString;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class CounterVerticle extends AbstractVerticle {
    private final static Logger LOGGER = LoggerFactory.getLogger(CounterVerticle.class);
    private final Counter counter;
    private final int port;

    private final static AsciiString CONTENT_LENGTH_20 = new AsciiString("20");

    public CounterVerticle(Counter counter, int port) {
        this.counter = counter;
        this.port = port;
    }

    @Override
    public void start() {
        counter.start();

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.get("/delta").handler((ctx) -> {
            CompletableFuture<Long> future = this.counter.delta(1);
            future.whenComplete((result, t) -> {
                if (t != null) {
                    ctx.fail(t);
                } else {
                    byte[] lb = new byte[LongToFixedWidthBytes.ARRAY_LENGTH];
                    byte[] resp = LongToFixedWidthBytes.write(result, lb);
                    ctx.response()
                            .putHeader("content-length", CONTENT_LENGTH_20)
                            .end(Buffer.buffer(resp));
                }
            });
        });

        router.get("/").handler((ctx) -> {
            CompletableFuture<Long> future = this.counter.read();
            future.whenComplete((result, t) -> {
                if (t != null) {
                    ctx.fail(t);
                } else {
                    byte[] lb = new byte[LongToFixedWidthBytes.ARRAY_LENGTH];
                    byte[] resp = LongToFixedWidthBytes.write(result, lb);
                    ctx.response()
                            .putHeader("content-length", CONTENT_LENGTH_20)
                            .end(Buffer.buffer(resp));
                }
            });
        });

        server.requestHandler(router)
                .listen(port)
                .onSuccess((h) -> {
                    LOGGER.info("Server started on port {}", port);
                });

    }

    @Override
    public void stop() {
        counter.close();
    }
}
