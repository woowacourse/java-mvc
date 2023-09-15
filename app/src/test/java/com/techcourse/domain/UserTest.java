package com.techcourse.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserTest {

    @Test
    void 비밀번호를_확인한다() {
        // given
        final User user = new User(1L, "hello", "pw", "my@google.com");

        // expect
        assertThat(user.checkPassword("pw")).isTrue();
    }
}
