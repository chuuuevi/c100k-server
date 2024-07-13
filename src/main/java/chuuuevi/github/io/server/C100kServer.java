package chuuuevi.github.io.server;

import chuuuevi.github.io.server.disruptor.Counter;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class C100kServer {

    private HttpServer server;
    private Counter counter;

    public C100kServer(int port, Counter counter) throws IOException {
        this.counter = counter;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.createContext("/").setHandler(exchange -> {
//            System.out.println("request "+ exchange.getRequestURI());

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
        });
        this.server.start();
        System.out.println("port=" + port);
    }

    public static int getHttpPort(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption(null, "http-port", true, "HTTP Port");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        return cmd.hasOption("http-port") ? Integer.parseInt(cmd.getOptionValue("http-port")) : 22222;
    }

    public static void main(String[] args) throws IOException, ParseException {
        Counter counter = new Counter();
        int port = getHttpPort(args);

        C100kServer server1 = new C100kServer(port, counter);
        C100kServer server2 = new C100kServer(port + 1, counter);
    }
}
