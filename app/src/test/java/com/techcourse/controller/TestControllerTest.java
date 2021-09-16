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

class TestControllerTest extends ControllerTest{

    @DisplayName("/get-test 응답에 성공한다.")
    @Test
    public void successGetTest() throws IOException {
        //when
        HttpURLConnection connection = connectTomcat("/get-test");
        InputStream inputStream = connection.getInputStream();
        int actual = connection.getResponseCode();

        //then
        assertThat(actual).isEqualTo(200);
        assertThat(checkFile(inputStream, "<title>get-test</title>")).isTrue();
    }

    @DisplayName("/post-test 응답에 성공한다.")
    @Test
    public void successPostTest() throws IOException {
        //when
        HttpURLConnection connection = connectTomcatPost("/post-test", APPLICATION_JSON_UTF8_VALUE);
        InputStream inputStream = connection.getInputStream();
        int actual = connection.getResponseCode();

        //then
        assertThat(actual).isEqualTo(200);
        assertThat(checkFile(inputStream, "<title>post-test</title>")).isTrue();
    }

}