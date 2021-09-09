package com.techcourse;

import java.io.File;
import java.util.stream.Stream;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwpApplication {

    private static final Logger log = LoggerFactory.getLogger(JwpApplication.class);

    private static final int DEFAULT_PORT = 8080;

    private static Tomcat tomcat;

    public static void main(String[] args) throws Exception {
        final int port = defaultPortIfNull(args);

        tomcat = new Tomcat();
        tomcat.setPort(port);

        addWebapp(tomcat);

        // 불필요한 설정은 skip
        skipBindOnInit(tomcat);

        tomcat.start();
    }

    private static int defaultPortIfNull(String[] args) {
        return Stream.of(args)
                     .findFirst()
                     .map(Integer::parseInt)
                     .orElse(DEFAULT_PORT);
    }

    private static Context addWebapp(Tomcat tomcat) {
        final String docBase = determinePath();

        log.info("configuring app with basedir: {}", docBase);

        return tomcat.addWebapp("/", docBase);
    }

    private static String determinePath() {
        final String absolutePath = new File("").getAbsolutePath();
        if (absolutePath.endsWith("/app")) {
            return absolutePath + "/webapp";
        }
        return absolutePath + "/app/webapp";
    }

    private static void skipBindOnInit(Tomcat tomcat) {
        final Connector connector = tomcat.getConnector();
        connector.setProperty("bindOnInit", "false");
    }

    static void stopTomcat() throws LifecycleException {
        tomcat.stop();
    }
}
