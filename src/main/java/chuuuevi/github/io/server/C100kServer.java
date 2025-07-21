package chuuuevi.github.io.server;

import chuuuevi.github.io.server.disruptor.Counter;
import chuuuevi.github.io.server.server.JDK21Server;
import chuuuevi.github.io.server.server.VertxServer;
import org.apache.commons.cli.*;

import java.io.IOException;

public class C100kServer {


    public static void main(String[] args) throws IOException, ParseException {
        Options options = new Options();
        options.addOption(null, "http-port", true, "HTTP Port");
        options.addOption(null, "server-type", true, "vertx or jdk21");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        int port = cmd.hasOption("http-port") ? Integer.parseInt(cmd.getOptionValue("http-port")) : 22222;
        String type = cmd.hasOption("server-type") ? cmd.getOptionValue("server-type") : "jdk21";

        Counter counter = new Counter();

        if (type.equals("jdk21")) {
            JDK21Server server = new JDK21Server(port, counter);
            server.start();
        } else {
            VertxServer server = new VertxServer(port, counter);
            server.start();
        }
    }
}
