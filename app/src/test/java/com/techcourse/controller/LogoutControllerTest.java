package com.techcourse.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.assertThat;

class LogoutControllerTest extends ControllerTest {
    @DisplayName("로그아웃 성공 시 /index.html로 리다이렉트한다.")
    @Test
    public void logoutSuccess() throws IOException {
        //when
        HttpURLConnection connection = connectTomcat("/logout");
        InputStream inputStream = connection.getInputStream();

        //then
        int actual = connection.getResponseCode();
        assertThat(actual).isEqualTo(200);
        assertThat(checkFile(inputStream, "<title>대시보드</title>")).isTrue();
    }
}