package com.techcourse.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void 로그인_성공_테스트() {
        User user = new User(1L, "gugu", "password", "gugu@woowa.com");

        boolean checked = user.checkPassword("password");

        assertThat(checked).isTrue();
    }

    @Test
    void 로그인_실패_테스트() {
        User user = new User(1L, "gugu", "password", "gugu@woowa.com");

        boolean checked = user.checkPassword("wrong");

        assertThat(checked).isFalse();
    }
}
