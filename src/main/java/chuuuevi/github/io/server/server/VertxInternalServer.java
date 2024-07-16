package chuuuevi.github.io.server.server;

import chuuuevi.github.io.server.disruptor.Counter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static io.vertx.core.Future.await;

public class VertxInternalServer extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(VertxInternalServer.class);

    private final int port;
    private final Counter counter;

    public VertxInternalServer(int port, Counter counter) {
        this.port = port;
        this.counter = counter;
    }

    @Override
    public void start() {
        try {
            Router router = Router.router(vertx);
            router.route("/").handler(ctx -> {

                counter.dealt(1);
                Long number = null;
                try {
                    number = counter.read();
                } catch (Exception e) {

                }

                byte[] respText = (number == null ? "-1" : number.toString()).getBytes(StandardCharsets.UTF_8);

                // This handler gets called for each request that arrives on the server
                HttpServerResponse response = ctx.response();
                response.putHeader("content-type", "text/plain");
                response.putHeader("Content-Length", String.valueOf(respText.length));
                response.setStatusCode(200);
                // Write to the response and end it
                await(response.end(Buffer.buffer(respText)));
            });

            vertx.createHttpServer()
                    .requestHandler(router)
                    .listen(this.port);

            log.info("vertx-server startup at {}", port);

        } catch (Throwable e) {
            log.error("start error : ", e);
        }
    }
}
