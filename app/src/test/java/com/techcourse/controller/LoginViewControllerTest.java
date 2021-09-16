package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.assertThat;

class LoginViewControllerTest extends ControllerTest {
    @DisplayName("login/view 응답에 성공한다.")
    @Test
    public void loginViewSuccess() throws IOException {
        //when
        HttpURLConnection connection = connectTomcat("/login/view");
        InputStream inputStream = connection.getInputStream();
        int actual = connection.getResponseCode();

        //then
        assertThat(actual).isEqualTo(200);
        assertThat(checkFile(inputStream, "<title>로그인</title>")).isTrue();
    }
}