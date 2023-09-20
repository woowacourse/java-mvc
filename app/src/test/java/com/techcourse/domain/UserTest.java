package com.techcourse.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void checkPassword() {
        // given
        String password = "luvJunpak";
        User user = new User(1L, "jerry", password, "kwonhyeonjae01@gmail.com");

        // when
        boolean checkPassword = user.checkPassword(password);

        // then
        Assertions.assertThat(checkPassword).isTrue();
    }
}
