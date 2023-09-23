package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LogoutControllerTest extends UsingTomcatTest {

    @DisplayName("요청시 302 상태코드와 / 로 리다이렉트한다.")
    @Test
    void save() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            //given
            final HttpPost httpPost = new HttpPost(tomcatUrl + "/logout");

            //when
            final HttpResponse response = httpClient.execute(httpPost);

            //then
            final int actualStatusCode = response.getStatusLine().getStatusCode();
            final String actualLocationHeaderValue = response.getFirstHeader("Location").getValue();

            assertThat(actualStatusCode).isEqualTo(302);
            assertThat(actualLocationHeaderValue).isEqualTo("/");
        } catch (Exception e) {
            Assertions.fail();
            e.printStackTrace();
        }
    }
}
