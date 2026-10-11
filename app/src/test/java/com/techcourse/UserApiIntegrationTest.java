package com.techcourse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserApiIntegrationTest {

    @TempDir
    static Path baseDirectory;

    private static Tomcat tomcat;
    private static String baseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void startServer() throws Exception {
        tomcat = new Tomcat();
        tomcat.setBaseDir(baseDirectory.toString());
        tomcat.setPort(0);
        tomcat.getConnector();
        final var context = tomcat.addContext("", baseDirectory.toString());
        context.addServletContainerInitializer((classes, servletContext) ->
                new DispatcherServletInitializer().onStartup(servletContext), null);
        tomcat.start();
        baseUrl = "http://localhost:" + tomcat.getConnector().getLocalPort();
    }

    @AfterAll
    static void stopServer() throws Exception {
        if (tomcat != null) {
            try {
                tomcat.stop();
            } finally {
                tomcat.destroy();
            }
        }
    }

    @Test
    @DisplayName("GET 사용자 조회는 비밀번호와 user 래퍼 없이 사용자 정보를 JSON으로 반환한다")
    void returnsUserAsJson() throws Exception {
        final String account = "마이찬-" + UUID.randomUUID();
        InMemoryUserRepository.save(new User(42, account, "secret", "user@example.com"));

        final var response = get("?account=" + URLEncoder.encode(account, StandardCharsets.UTF_8));

        assertThat(response.statusCode()).isEqualTo(200);
        assertJsonContentType(response);
        final var body = objectMapper.readTree(response.body());
        assertThat(body.size()).isEqualTo(3);
        assertThat(body.get("id").asLong()).isEqualTo(42);
        assertThat(body.get("account").asText()).isEqualTo(account);
        assertThat(body.get("email").asText()).isEqualTo("user@example.com");
        assertThat(body.has("password")).isFalse();
        assertThat(body.has("user")).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 계정은 404와 JSON 오류 메시지를 반환한다")
    void returnsNotFound() throws Exception {
        final var response = get("?account=missing-" + UUID.randomUUID());

        assertError(response, 404, "사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("계정 파라미터가 없으면 400과 JSON 오류 메시지를 반환한다")
    void rejectsMissingAccount() throws Exception {
        assertError(get(""), 400, "account는 필수입니다.");
    }

    @Test
    @DisplayName("빈 계정이나 공백 계정은 400과 JSON 오류 메시지를 반환한다")
    void rejectsBlankAccount() throws Exception {
        assertError(get("?account="), 400, "account는 필수입니다.");
        assertError(get("?account=%20%20"), 400, "account는 필수입니다.");
    }

    private HttpResponse<String> get(final String query) throws Exception {
        try (final var client = HttpClient.newHttpClient()) {
            final var request = HttpRequest.newBuilder(URI.create(baseUrl + "/api/user" + query))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        }
    }

    private void assertError(final HttpResponse<String> response, final int status, final String message) throws Exception {
        assertThat(response.statusCode()).isEqualTo(status);
        assertJsonContentType(response);
        final var body = objectMapper.readTree(response.body());
        assertThat(body.size()).isEqualTo(1);
        assertThat(body.get("message").asText()).isEqualTo(message);
    }

    private void assertJsonContentType(final HttpResponse<String> response) {
        assertThat(response.headers().firstValue("Content-Type"))
                .hasValue("application/json;charset=UTF-8");
    }
}
