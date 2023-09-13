package com.techcourse.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @DisplayName("사용자 비밀번호 일치 여부를 확인한다.")
    @Test
    void checkPassword() {
        final var correct = "password";
        User user = new User(1L, "gugu", correct, "gugu@woowa.com");

        assertSoftly(softly -> {
            assertThat(user.checkPassword(correct)).isTrue();
            assertThat(user.checkPassword("wrong")).isFalse();
        });
    }

}
