package com.techcourse.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserTest {

    @Test
    void 비밀번호가_일치하는지_확인할_수_있다() {
        // given
        User user = new User(1L, "계정", "비밀번호", "이메일");

        // expect
        assertThat(user.checkPassword("다른비밀번호")).isFalse();
    }

}
