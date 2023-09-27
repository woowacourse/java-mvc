package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import web.org.springframework.http.MediaType;

@DisplayName("/api/user 경로에 대한 테스트")
class UserControllerTest extends UsingTomcatTest {

    @DisplayName("account 파라미터에 해당하는 유저를 조회하여 반환한다.")
    @Test
    void show() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            //given
            final HttpGet httpGet = new HttpGet(tomcatUrl + "/api/user?account=gugu");

            //when
            final HttpResponse response = httpClient.execute(httpGet);

            //then
            final String contentType = response.getFirstHeader("Content-Type").getValue();
            final String body = EntityUtils.toString(response.getEntity());

            assertThat(contentType).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
            assertThat(body).isEqualTo("{\"account\":\"gugu\"}");
        }
    }
}
