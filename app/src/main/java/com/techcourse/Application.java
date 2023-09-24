package com.techcourse;

import java.io.IOException;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.TomcatStarter;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static final int DEFAULT_PORT = 8080;

    private static final String WEBAPP_DIR_LOCATION = "app/src/main/webapp/";

    public static void main(final String[] args) {
        final int port = defaultPortIfNull(args);
        final var tomcat = new TomcatStarter(WEBAPP_DIR_LOCATION, port);
        log.info("configuring app with basedir: {}", WEBAPP_DIR_LOCATION);

        tomcat.start();
        stop(tomcat);
    }

    private static int defaultPortIfNull(final String[] args) {
        return Stream.of(args)
                .findFirst()
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);
    }

    private static void stop(final TomcatStarter tomcat) {
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
