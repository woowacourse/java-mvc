package com.techcourse;

import java.io.File;
import java.util.stream.Stream;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static final int DEFAULT_PORT = 8080;

    public static void main(final String[] args) throws Exception {
        int port = defaultPortIfNull(args);

        var tomcat = new Tomcat();
        tomcat.setConnector(createConnector(port));
        var docBase = new File("app/src/main/webapp/").getAbsolutePath();
        tomcat.addWebapp("", docBase);
        log.info("configuring app with basedir: {}", docBase);

        tomcat.start();
        tomcat.getServer().await();
    }

    private static Connector createConnector(final int port) {
        var connector = new Connector();
        connector.setPort(port);
        connector.setProperty("bindOnInit", "false");
        return connector;
    }

    private static int defaultPortIfNull(final String[] args) {
        return Stream.of(args)
                .findFirst()
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);
    }
}
