package servlet.com.example;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletApplication {

    private static final Logger log = LoggerFactory.getLogger(ServletApplication.class);

    public static void main(String[] args) {
        final var tomcatStarter = new TomcatStarter();
        tomcatStarter.start();
        stop(tomcatStarter);
    }

    private static void stop(final TomcatStarter tomcat) {
        try {
            // make the application wait until we press any key.
            System.in.read();
        } catch (IOException e) {
            log.error("e: ", e);
        } finally {
            tomcat.stop();
        }
    }
}
