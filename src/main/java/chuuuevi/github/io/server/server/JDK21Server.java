package chuuuevi.github.io.server.server;

import chuuuevi.github.io.server.disruptor.Counter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class JDK21Server {

    private static final Logger log = LoggerFactory.getLogger(JDK21Server.class);

    private HttpServer server;
    private final Counter counter;
    private final Executor executor;

    public JDK21Server(int port, Counter counter, boolean asyncHandle) throws IOException {
        this.counter = counter;
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.setExecutor(this.executor);
        if (asyncHandle) {
            server.createContext("/").setHandler(this::handleAsync);
        } else {
            server.createContext("/").setHandler(this::handleSync);
        }

        this.server.start();
        log.info("jdk21-server startup at {}, async={}", port, asyncHandle);
    }

    private void handleSync(HttpExchange exchange) throws IOException {

        counter.dealt(1);
        Long number = null;
        try {
            number = counter.read();
        } catch (Exception e) {

        }
        byte[] respText = (number == null ? "-1" : number.toString()).getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(200, respText.length);
        try (var os = exchange.getResponseBody()) {
            os.write(respText);
        }
    }


    private void handleAsync(HttpExchange exchange) throws IOException {
        counter.dealt(1);

        counter.readAsync().thenAcceptAsync(
                (number) -> {
                    byte[] respText = (number == null ? "-1" : number.toString()).getBytes(StandardCharsets.UTF_8);
                    try {
                        exchange.sendResponseHeaders(200, respText.length);
                        try (var os = exchange.getResponseBody()) {
                            os.write(respText);
                        }
                    } catch (IOException e) {

                    }
                },
                this.executor
        );
    }
}
