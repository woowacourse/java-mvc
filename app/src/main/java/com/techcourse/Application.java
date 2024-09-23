package com.techcourse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.Stream;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = defaultPortIfNull(args);
        TomcatStarter tomcat = new TomcatStarter(port);
        log.info("configuring app with basedir: {}", TomcatStarter.WEBAPP_DIR_LOCATION);

        tomcat.start();
        stop(tomcat);
    }

    private static int defaultPortIfNull(String[] args) {
        return Stream.of(args)
                .findFirst()
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);
    }

    private static void stop(TomcatStarter tomcat) {
        try {
            // make the application wait until we press any key.
            System.in.read();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info("web server stop.");
            tomcat.stop();
        }
    }
}
