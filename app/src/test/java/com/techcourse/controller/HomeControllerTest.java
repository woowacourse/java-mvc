package com.techcourse.controller;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/ 경로 요청 테스트")
class HomeControllerTest extends UsingTomcatTest {
    @DisplayName("GET 요청시 200 상태코드 와 함께 메인 페이지로 응답한다.")
    @Test
    void save() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            //given
            final HttpGet httpGet = new HttpGet(tomcatUrl + "/");

            //when
            final HttpResponse response = httpClient.execute(httpGet);

            //then
            final int actualStatusCode = response.getStatusLine().getStatusCode();
            final Header contentTypeHeader = response.getFirstHeader("Content-Type");

            assertThat(actualStatusCode).isEqualTo(200);
            assertThat(contentTypeHeader.getValue()).isEqualTo("text/html;charset=UTF-8");
        } catch (Exception e) {
            Assertions.fail();
            e.printStackTrace();
        }
    }
}
