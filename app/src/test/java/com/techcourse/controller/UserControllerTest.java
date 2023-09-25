package com.techcourse.controller;

import com.techcourse.TomcatStarter;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserControllerTest {

    @Test
    void 유저_정보가_없으면_null이다() throws Exception {
        HttpResponse<String> response = send(HttpRequest.newBuilder()
                .uri(new URI("http://localhost:33333/api/user/me"))
                .GET()
                .build());

        assertThat(response.body()).isEqualTo("{\"user\":null}");
    }

    private HttpResponse<String> send(HttpRequest httpRequest) throws Exception {
        Thread thread = new Thread(this::runServerIgnoringException);
        thread.start();
        Thread.sleep(3000);

        return HttpClient.newHttpClient()
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private void runServerIgnoringException() {
        try {
            new TomcatStarter("src/main/webapp/", 33333).start();
        } catch (Exception ignored) {
        }
    }
}
