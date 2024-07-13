package chuuuevi.github.io.server;

import com.sun.net.httpserver.HttpServer;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class C100kServer {


    public static void main(String[] args) throws IOException, ParseException {

        Options options = new Options();

        options.addOption(null, "http-port", true, "HTTP Port");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        int port = cmd.hasOption("http-port") ? Integer.parseInt(cmd.getOptionValue("http-port")) : 22222;

        var body = "hello world".getBytes();
        var server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.createContext("/").setHandler(exchange -> {
//            String text = IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, body.length);
            try (var os = exchange.getResponseBody()) {
                os.write(body);
            }
        });
        server.start();
        System.out.println("port=" + port);
    }
}
