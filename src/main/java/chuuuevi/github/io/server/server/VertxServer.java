package chuuuevi.github.io.server.server;

import chuuuevi.github.io.server.disruptor.Counter;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class VertxServer {

    private static final Logger log = LoggerFactory.getLogger(VertxServer.class);
    private final HttpServer server;
    private final Router router;

    public VertxServer(int port, Counter counter) {
        Vertx vertx = Vertx.vertx();

        HttpServerOptions options = new HttpServerOptions();
        options.setPort(port);
        this.server = vertx.createHttpServer(options);
        this.router = Router.router(vertx);

        this.router.get("/").handler(ctx -> {
            counter.dealt(1);
            Long number = null;
            try {
                number = counter.readAsync()
                        .get();
            } catch (Exception e) {

            }
            byte[] respText = (number == null ? "-1" : number.toString()).getBytes(StandardCharsets.UTF_8);
            ctx.response()
                    .putHeader("content-type", "text/plain")
                    .putHeader("Content-Length", String.valueOf(respText.length))
                    .setStatusCode(200)
                    .end(Buffer.buffer(respText))
            ;
        });
    }

    public void start() {
        this.server
                .requestHandler(this.router)
                .listen();
    }
}
