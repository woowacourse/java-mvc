package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("/login 경로 요청 테스트")
class LoginControllerTest extends UsingTomcatTest {

    @DisplayName("POST로 로그인 정보를 form-data 형식으로 본문에 담아 요청시 유저가 있을 경우, 302 응답 코드와 Location 헤더에 /index.jsp 를 담아 응답한다.")
    @Test
    void login_success() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            //given
            final HttpPost httpPost = new HttpPost(tomcatUrl + "/login");

            final List<BasicNameValuePair> formData = new ArrayList<>();
            formData.add(new BasicNameValuePair("account", "gugu"));
            formData.add(new BasicNameValuePair("password", "password"));

            final UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formData);
            httpPost.setEntity(urlEncodedFormEntity);

            //when
            final HttpResponse response = httpClient.execute(httpPost);

            //then
            final int actualStatusCode = response.getStatusLine().getStatusCode();
            final String actualLocationHeaderValue = response.getFirstHeader("Location").getValue();

            assertThat(actualStatusCode).isEqualTo(302);
            assertThat(actualLocationHeaderValue).isEqualTo("/index.jsp");
        } catch (Exception e) {
            Assertions.fail();
            e.printStackTrace();
        }
    }

    @DisplayName("POST로 로그인 정보를 form-data 형식으로 본문에 담아 요청시 유저가 없을 경우, 302 응답 코드와 Location 헤더에 /401.jsp 를 담아 응답한다.")
    @Test
    void login_fail() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            //given
            final HttpPost httpPost = new HttpPost(tomcatUrl + "/login");

            final List<BasicNameValuePair> formData = new ArrayList<>();
            formData.add(new BasicNameValuePair("account", "gugu"));
            formData.add(new BasicNameValuePair("password", "wrong"));

            final UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formData);
            httpPost.setEntity(urlEncodedFormEntity);

            //when
            final HttpResponse response = httpClient.execute(httpPost);

            //then
            final int actualStatusCode = response.getStatusLine().getStatusCode();
            final String actualLocationHeaderValue = response.getFirstHeader("Location").getValue();

            assertThat(actualStatusCode).isEqualTo(302);
            assertThat(actualLocationHeaderValue).isEqualTo("/401.jsp");
        } catch (Exception e) {
            Assertions.fail();
            e.printStackTrace();
        }
    }
}
