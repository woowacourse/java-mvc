package com.techcourse.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @DisplayName("password가 맞는지 확인한다. - 성공")
    @Test
    void checkPassword() {
        // given
        User user = new User("joanne", "1234", "joanne@woowahan.com");

        // when
        assertThat(user.checkPassword("1234")).isTrue();
    }

    @DisplayName("password가 맞는지 확인한다. - 실패")
    @Test
    void checkPasswordFaiiled() {
        // given
        User user = new User("joanne", "1234", "joanne@woowahan.com");

        // when
        assertThat(user.checkPassword("123456")).isFalse();
    }

    @DisplayName("account를 가져온다.")
    @Test
    void getAccount() {
        // given
        User user = new User("joanne", "1234", "joanne@woowahan.com");

        assertThat(user.getAccount()).isEqualTo("joanne");
    }

    @DisplayName("")
    @Test
    void setId() {
        // given
        User user = new User("joanne", "1234", "joanne@woowahan.com");
        User compareUser = new User(1, "joanne", "1234", "joanne@woowahan.com");

        // when
        user.setId(1);

        // then
        assertThat(user.equals(compareUser)).isTrue();
    }
}
