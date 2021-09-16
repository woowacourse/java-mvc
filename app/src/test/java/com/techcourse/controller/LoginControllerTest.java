package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

class LoginControllerTest extends ControllerTest{

    @DisplayName("로그인에 성공한다.")
    @Test
    public void loginSuccess() throws IOException {
        //when
        HttpURLConnection connection = connectTomcatPost("/login?account=gugu&password=password",
                APPLICATION_JSON_UTF8_VALUE);

        //then
        int actual = connection.getResponseCode();
        assertThat(actual).isEqualTo(200);
    }

    @DisplayName("회원가입 되지 않은 회원으로 로그인하면 실패한다.")
    @Test
    public void loginFailNotRegistered() throws IOException {
        //given
        HttpURLConnection connection = connectTomcatPost("/login?account=NotRegisteredUser&password=test",
                APPLICATION_JSON_UTF8_VALUE);

        //when
        int actual = connection.getResponseCode();

        //then
        assertThat(actual).isEqualTo(500);
    }

    @DisplayName("비밀번호가 틀릴 경우 로그인에 실패하고, 401페이지로 리다이렉트한다.")
    @Test
    public void loginFailInvalidPassword() throws IOException {
        //given
        InMemoryUserRepository.save(new User("test2", "test2", "test@email.com"));

        //when
        HttpURLConnection connection = connectTomcatPost("/login?account=test2&password=password",
                APPLICATION_JSON_UTF8_VALUE);
        InputStream inputStream = connection.getInputStream();
        int actual = connection.getResponseCode();

        //then
        assertThat(actual).isEqualTo(200);
        assertThat(checkFile(inputStream, "<title>401 Error - SB Admin</title>")).isTrue();
    }
}