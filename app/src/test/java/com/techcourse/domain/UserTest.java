package com.techcourse.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class UserTest {

    @Test
    void 입력한_비밀번호가_사용자_정보와_일치하는지_확인할_수_있다() {
        //given
        User user = new User(1, "gugu", "password", "test@email.com");

        //when
        boolean result = user.checkPassword("password");

        //then
        assertThat(result).isTrue();
    }

    @Test
    void 입력한_비밀번호가_사용자_정보와_불일치하는지_확인할_수_있다() {
        //given
        User user = new User(1, "gugu", "password", "test@email.com");

        //when
        boolean result = user.checkPassword("wrong password");

        //then
        assertThat(result).isFalse();
    }
}
