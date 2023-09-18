package com.techcourse.controller;

import com.techcourse.TomcatStarter;
import java.io.IOException;
import java.net.ServerSocket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class UsingTomcatTest {

    protected static String tomcatUrl;
    protected static TomcatStarter tomcatStarter;

    public static int getLocalPort() {
        try (final ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void setUp() {
        final int port = getLocalPort();
        tomcatStarter = new TomcatStarter("src/main/webapp/", port);
        tomcatUrl = "http://localhost:" + port;
        tomcatStarter.start();
    }

    @AfterAll
    static void cleanUp() {
        tomcatStarter.stop();
    }
}
