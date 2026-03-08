package chuuuevi.github.io.server;

import chuuuevi.github.io.server.biz.Counter;
import chuuuevi.github.io.server.server.CounterVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.apache.commons.cli.*;

import java.io.IOException;

public class C100kServer {

    public static void main(String[] args) throws IOException, ParseException {
        Options options = new Options();
        options.addOption(null, "http-port", true, "HTTP Port");
        options.addOption(null, "prefer-native-transport", true, "HTTP Port");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        int port = cmd.hasOption("http-port") ? Integer.parseInt(cmd.getOptionValue("http-port")) : 22222;
        boolean preferNativeTransport = cmd.hasOption("http-port");

        Counter counter = new Counter();

        CounterVerticle cv = new CounterVerticle(counter, port);

        Vertx.vertx(new VertxOptions().
                setPreferNativeTransport(preferNativeTransport)
        ).deployVerticle(cv);
    }
}
