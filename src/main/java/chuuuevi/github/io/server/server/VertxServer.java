package chuuuevi.github.io.server.server;

import chuuuevi.github.io.server.disruptor.Counter;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class VertxServer {

    private static final Logger log = LoggerFactory.getLogger(VertxServer.class);

    public VertxServer(int port, Counter counter) {
        HttpServer server = Vertx.vertx().createHttpServer();
        server.requestHandler(request -> {

            counter.dealt(1);
            Long number = null;
            try {
                number = counter.read();
            } catch (Exception e) {

            }


            byte[] respText = (number == null ? "-1" : number.toString()).getBytes(StandardCharsets.UTF_8);


            // This handler gets called for each request that arrives on the server
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "text/plain");
            response.putHeader("Content-Length", String.valueOf(respText.length));
            response.setStatusCode(200);
            // Write to the response and end it
            response.end(Buffer.buffer(respText));
        });

        server.listen(port);

        log.info("vertx-server startup at {}", port);

    }
}
