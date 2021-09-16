package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterViewControllerTest extends ControllerTest {

    @DisplayName("/register/view 응답에 성공한다.")
    @Test
    public void registerViewSuccess() throws IOException {
        //given
        InMemoryUserRepository.save(new User("test1", "test1", "test@email.com"));

        //when
        HttpURLConnection connection = connectTomcat("/register/view");
        InputStream inputStream = connection.getInputStream();
        int actual = connection.getResponseCode();

        //then
        assertThat(actual).isEqualTo(200);
        assertThat(checkFile(inputStream, "<title>회원가입</title>")).isTrue();
    }
}