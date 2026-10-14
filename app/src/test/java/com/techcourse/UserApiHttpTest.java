package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserApiHttpTest {

    @TempDir
    Path directory;

    @Test
    void returnsJsonForSuccessAndExpectedClientErrors() throws Exception {
        verifyResponses("");
    }

    @Test
    void returnsSameApiContractUnderContextPath() throws Exception {
        verifyResponses("/mvc");
    }

    private void verifyResponses(String contextPath) throws Exception {
        final String account = "구구-" + UUID.randomUUID();
        InMemoryUserRepository.save(new User(4, account, "secret", "http@example.com"));
        final var tomcat = new Tomcat();
        tomcat.setBaseDir(directory.resolve("tomcat").toString());
        tomcat.setPort(0);
        tomcat.getConnector().setProperty("address", "127.0.0.1");
        final var docBase = Files.createDirectories(directory.resolve("webapp"));
        final var context = tomcat.addContext(contextPath, docBase.toString());
        final var wrapper = Tomcat.addServlet(context, "dispatcher",
                new DispatcherServlet("com.techcourse.controller"));
        wrapper.setLoadOnStartup(1);
        context.addServletMappingDecoded("/", "dispatcher");

        try {
            tomcat.start();
            final String endpoint = "http://127.0.0.1:" + tomcat.getConnector().getLocalPort()
                    + contextPath + "/api/user";
            try (final var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build()) {
                final var success = get(client, endpoint + "?account="
                        + URLEncoder.encode(account, StandardCharsets.UTF_8));
                assertJsonResponse(success, 200);
                assertThat(success.body()).isEqualTo("{\"user\":{\"account\":\"" + account + "\"}}");

                for (final String suffix : new String[]{"", "?account=", "?account=%20%09"}) {
                    final var invalid = get(client, endpoint + suffix);
                    assertJsonResponse(invalid, 400);
                    assertThat(invalid.body()).contains("\"error\"", "\"code\":\"INVALID_ACCOUNT\"",
                            "\"message\":\"account is required\"");
                }

                final var unknown = get(client, endpoint + "?account=" + UUID.randomUUID());
                assertJsonResponse(unknown, 404);
                assertThat(unknown.body()).contains("\"error\"", "\"code\":\"USER_NOT_FOUND\"",
                        "\"message\":\"user not found\"");
            }
        } finally {
            tomcat.stop();
            tomcat.destroy();
        }
    }

    private HttpResponse<String> get(HttpClient client, String url) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(5)).GET().build(), HttpResponse.BodyHandlers.ofString());
    }

    private void assertJsonResponse(HttpResponse<String> response, int status) {
        assertThat(response.statusCode()).isEqualTo(status);
        assertThat(response.headers().firstValue("content-type").orElseThrow())
                .containsIgnoringCase("application/json").containsIgnoringCase("charset=UTF-8");
        assertThat(response.body()).startsWith("{").doesNotContain("<html");
    }
}
