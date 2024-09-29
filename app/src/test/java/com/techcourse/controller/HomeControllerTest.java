package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.TomcatStarter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HomeControllerTest {

    private static final String URL = "http://localhost:8080";

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
    @DisplayName("홈 화면을 요청시 대시보드 페이지를 응답한다.")
    void getHome() throws IOException, InterruptedException {
        // given
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        client.close();

        // then
        assertThat(response.body()).contains("<title>대시보드</title>");
    }
}