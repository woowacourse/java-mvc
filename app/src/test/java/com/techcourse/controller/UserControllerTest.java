package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.TomcatStarter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    private static final int PORT = 18080;

    @Test
    void getUserReturnsJson() throws Exception {
        final var tomcat = new TomcatStarter("src/main/webapp/", PORT);

        try (final var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build()) {
            tomcat.start();

            final var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + PORT + "/api/user?account=gugu"))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            final var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(response.headers().firstValue("Content-Type"))
                    .hasValue("application/json;charset=UTF-8");
            assertThat(response.body()).isEqualTo("{\"account\":\"gugu\"}");
        } finally {
            tomcat.stop();
        }
    }
}
