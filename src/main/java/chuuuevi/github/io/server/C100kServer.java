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


    public static void main(String[] args) throws IOException, ParseException {

        System.out.println("args="+ Arrays.toString(args));

        Options options = new Options();

        options.addOption(null, "http-port", true, "HTTP Port");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        int port = cmd.hasOption("http-port") ? Integer.parseInt(cmd.getOptionValue("http-port")) : 22222;

        Counter counter = new Counter();

        var server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
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
        server.start();
        System.out.println("port=" + port);
    }
}
