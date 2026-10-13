package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class JsonViewFailureHttpTest {

    @TempDir
    Path directory;

    @Test
    void returnsServerErrorWithoutPartialJsonWhenSerializationFails() throws Exception {
        final var tomcat = new Tomcat();
        tomcat.setBaseDir(directory.resolve("tomcat").toString());
        tomcat.setPort(0);
        tomcat.getConnector().setProperty("address", "127.0.0.1");
        final var docBase = Files.createDirectories(directory.resolve("webapp"));
        final var context = tomcat.addContext("", docBase.toString());
        final var wrapper = Tomcat.addServlet(context, "dispatcher",
                new DispatcherServlet("serializationfailurehandlers"));
        wrapper.setLoadOnStartup(1);
        context.addServletMappingDecoded("/", "dispatcher");

        try {
            tomcat.start();
            final var endpoint = URI.create("http://127.0.0.1:"
                    + tomcat.getConnector().getLocalPort() + "/serialization-failure");
            try (final var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build()) {
                final var response = client.send(HttpRequest.newBuilder(endpoint)
                                .timeout(Duration.ofSeconds(5)).GET().build(),
                        HttpResponse.BodyHandlers.ofString());

                assertThat(response.statusCode()).isEqualTo(500);
                assertThat(response.body()).doesNotContain("{\"user\":");
            }
        } finally {
            tomcat.stop();
            tomcat.destroy();
        }
    }
}
