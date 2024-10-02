package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.techcourse.TomcatStarter;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    private static final String URL = "http://localhost:8080/api/user";

    private TomcatStarter tomcat;

    @BeforeEach
    void setUp() {
        tomcat = new TomcatStarter("src/main/webapp/", 8080);
        tomcat.start();
    }

    @AfterEach
    void tearDown() {
        tomcat.stop();
    }

    @Test
    @DisplayName("올바른 파라미터로 요청 시 유저 정보를 반환한다.")
    void getUser() throws Exception {
        // given
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "?account=gugu"))
                .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        client.close();

        // then
        assertThat(response.body()).isEqualTo("{\"user\":{\"account\":\"gugu\"}}");
    }

    @Test
    @DisplayName("존재하지 않는 계정 정보를 보내면 400을 반환한다.")
    void getUserWithNonExistReturnBadRequest() throws Exception {
        // given
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "?account=seyang"))
                .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        client.close();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST),
                () -> assertThat(response.body()).isEqualTo("User not found: seyang")
        );
    }

    @Test
    @DisplayName("빈 계정 정보를 보내면 400을 반환한다.")
    void getUserWithEmptyReturnBadRequest() throws Exception {
        // given
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "?account="))
                .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        client.close();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST),
                () -> assertThat(response.body()).isEqualTo("Please input account as parameter")
        );
    }
}
