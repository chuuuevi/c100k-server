package chuuuevi.github.io.server;

import chuuuevi.github.io.server.disruptor.Counter;
import chuuuevi.github.io.server.server.JDK21Server;
import chuuuevi.github.io.server.server.VertxServer;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class C100kServer {

    private static final Logger log = LoggerFactory.getLogger(C100kServer.class);


    public static void main(String[] args) throws IOException, ParseException {


        Options options = new Options();
        options.addOption(null, "http-port", true, "HTTP Port");
        options.addOption(null, "server-type", true, "vertx or jdk21");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        int port = cmd.hasOption("http-port") ? Integer.parseInt(cmd.getOptionValue("http-port")) : 22222;
        String type = cmd.hasOption("server-type") ? cmd.getOptionValue("server-type") : "jdk21";

        Counter counter = new Counter();

        if (type .equals("jdk21")) {
            new JDK21Server(port, counter, true);
        } else {
            new VertxServer(port, counter);
        }
    }
}
