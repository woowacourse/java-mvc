package com.techcourse.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

class RegisterControllerTest extends ControllerTest {

    @DisplayName("회원가입 성공 시 /index.html로 리다이렉트한다.")
    @Test
    public void loginSuccess() throws IOException {
        //when
        HttpURLConnection connection = connectTomcatPost("/register?account=test&password=password&email=tset@tset.com",
                APPLICATION_JSON_UTF8_VALUE);
        InputStream inputStream = connection.getInputStream();

        //then
        int actual = connection.getResponseCode();
        assertThat(actual).isEqualTo(200);
        assertThat(checkFile(inputStream, "<title>대시보드</title>")).isTrue();
    }

}