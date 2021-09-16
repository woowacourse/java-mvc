package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest extends ControllerTest{
    @DisplayName("유저 조회에 성공한다.")
    @Test
    public void successUserGet() throws IOException {
        //given
        InMemoryUserRepository.save(new User("test1234", "test1234", "test@email.com"));

        //when
        HttpURLConnection connection = connectTomcat("/api/user?account=test1234");
        int actual = connection.getResponseCode();

        //then
        assertThat(actual).isEqualTo(200);
    }

    @DisplayName("존재하지 않는 유저를 조회한다.")
    @Test
    public void failUserGet() throws IOException {
        //when
        HttpURLConnection connection = connectTomcat("/api/user?account=who");
        int actual = connection.getResponseCode();

        //then
        assertThat(actual).isEqualTo(500);
    }
}