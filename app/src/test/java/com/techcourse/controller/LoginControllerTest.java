package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.techcourse.TomcatStarter;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginControllerTest {

    private static final String URL = "http://localhost:8080/login";
    private static final String CONTENT_TYPE_URLENCODED = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_NAME = "Content-Type";

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
    @DisplayName("올바른 계정 정보로 로그인 요청 시 대시보드 페이지로 리디렉션을 응답한다.")
    void postLogin() throws Exception {
        // given
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header(CONTENT_TYPE_NAME, CONTENT_TYPE_URLENCODED)
                .POST(BodyPublishers.ofString("account=gugu&password=password"))
                .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        client.close();

        // then
        assertAll(
                () -> assertThat(response.headers().firstValue("Location")).hasValue("/index.jsp"),
                () -> assertThat(response.statusCode()).isEqualTo(HttpServletResponse.SC_FOUND)
        );
    }

    @Test
    @DisplayName("존재하지 않는 계정으로 요청 시 401 페이지로 리디렉션을 응답한다.")
    void getLoginWithNonAccount() throws Exception {
        // given
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header(CONTENT_TYPE_NAME, CONTENT_TYPE_URLENCODED)
                .POST(BodyPublishers.ofString("account=seyang&password=password"))
                .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        client.close();

        // then
        assertAll(
                () -> assertThat(response.headers().firstValue("Location")).hasValue("/401.jsp"),
                () -> assertThat(response.statusCode()).isEqualTo(HttpServletResponse.SC_FOUND)
        );
    }

    @Test
    @DisplayName("틀린 비밀번호로 요청 시 401 페이지로 리디렉션을 응답한다.")
    void getLoginWithInvalidPassword() throws Exception {
        // given
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header(CONTENT_TYPE_NAME, CONTENT_TYPE_URLENCODED)
                .POST(BodyPublishers.ofString("account=gugu&password=pw"))
                .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        response.sslSession();
        client.close();

        // then
        assertAll(
                () -> assertThat(response.headers().firstValue("Location")).hasValue("/401.jsp"),
                () -> assertThat(response.statusCode()).isEqualTo(HttpServletResponse.SC_FOUND)
        );
    }
}
