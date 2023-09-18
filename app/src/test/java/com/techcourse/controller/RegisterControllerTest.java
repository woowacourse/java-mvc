package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("/register 경로 요청 테스트")
class RegisterControllerTest extends UsingTomcatTest {

    @DisplayName("GET 로 요청을 보내 200 상태코드와 회원가입 페이지를 본문에 담아 반환한다.")
    @Test
    void show() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            //given
            final HttpGet httpGet = new HttpGet(tomcatUrl + "/register");

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

    @DisplayName("회원가입 정보를 form-data 형식으로 본문에 담아 POST 요청시 유저를 저장하고 302 상태코드, Location 헤더에 /index.html 를 담아 응답한다.")
    @Test
    void save() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            //given
            final HttpPost httpPost = new HttpPost(tomcatUrl + "/register");

            final List<BasicNameValuePair> formData = new ArrayList<>();
            formData.add(new BasicNameValuePair("account", "split"));
            formData.add(new BasicNameValuePair("email", "split@daum.net"));
            formData.add(new BasicNameValuePair("password", "password"));

            final UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formData);
            httpPost.setEntity(urlEncodedFormEntity);

            //when
            final HttpResponse response = httpClient.execute(httpPost);

            //then
            final String body = EntityUtils.toString(response.getEntity());
            final int actualStatusCode = response.getStatusLine().getStatusCode();
            final String actualLocationHeaderValue = response.getFirstHeader("Location").getValue();

            assertThat(actualStatusCode).isEqualTo(302);
            assertThat(actualLocationHeaderValue).isEqualTo("/index.jsp");
            assertThat(body).isEmpty();
        } catch (Exception e) {
            Assertions.fail();
            e.printStackTrace();
        }
    }
}
