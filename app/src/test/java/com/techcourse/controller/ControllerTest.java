package com.techcourse.controller;

import nextstep.web.support.RequestMethod;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public abstract class ControllerTest {
    private static final int DEFAULT_PORT = 8081;

    @BeforeEach
    public void setUp() throws LifecycleException {
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(DEFAULT_PORT);

        addWebapp(tomcat);
        skipBindOnInit(tomcat);

        tomcat.start();
    }

    protected static void skipBindOnInit(Tomcat tomcat) {
        final Connector connector = tomcat.getConnector();
        connector.setProperty("bindOnInit", "false");
    }

    private static Context addWebapp(Tomcat tomcat) {
        final String docBase = new File("webapp/").getAbsolutePath();
        return tomcat.addWebapp("/", docBase);
    }

    protected HttpURLConnection connectTomcat(String path) throws IOException {
        final URL url = new URL("http://localhost:" + DEFAULT_PORT + path);
        return (HttpURLConnection) url.openConnection();
    }

    protected HttpURLConnection connectTomcatPost(String path, String contentType) throws IOException {
        final URL url = new URL("http://localhost:" + DEFAULT_PORT + path);
        final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(RequestMethod.POST.name());
        httpURLConnection.setRequestProperty("Content-Type", contentType);
        return httpURLConnection;
    }

    protected boolean checkFile(InputStream inputStream, String title) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            if (currentLine.contains(title)) {
                return true;
            }
        }
        return false;
    }
}
