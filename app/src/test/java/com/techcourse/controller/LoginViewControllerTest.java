package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("/login/view 경로 요청 테스트")
class LoginViewControllerTest extends UsingTomcatTest {

    @DisplayName("GET 요청시 200 상태코드 와 함께 로그인 페이지로 응답한다.")
    @Test
    void save() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            //given
            final HttpPost httpPost = new HttpPost(tomcatUrl + "/login/view");

            //when
            final HttpResponse response = httpClient.execute(httpPost);

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
