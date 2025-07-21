package chuuuevi.github.io.server.server;

import chuuuevi.github.io.server.disruptor.Counter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class JDK21Server {

    private static final Logger log = LoggerFactory.getLogger(JDK21Server.class);
    private final HttpServer server;
    private final Counter counter;

    public JDK21Server(int port, Counter counter) throws IOException {
        this.counter = counter;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        this.server.createContext("/").setHandler(this::handleRead);
    }

    public void start() {
        this.server.start();
        log.info("jdk21-server startup at {}", this.server.getAddress());
    }

    private void handleRead(HttpExchange exchange) throws IOException {
        counter.dealt(1);
        Long number = null;
        try {
            number = counter.readAsync()
                    .get();
        } catch (Exception e) {
        }

        byte[] respText = (number == null ? "-1" : number.toString()).getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, respText.length);
        try {

            try (var os = exchange.getResponseBody()) {
                os.write(respText);
            }
        } catch (IOException e) {
        }
    }
}
