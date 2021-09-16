package com.techcourse.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User 도메인 테스트")
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "gugu", "password", "hkkang@woowahan.com");
    }

    @DisplayName("id기준 equals() & hashCode() 테스트")
    @Test
    void equalsAndHashCodeById() {
        // given
        final User otherUser = new User(1L, "inbi", "asdfasdf", "inbi@email.com");

        // when
        // then
        assertThat(user)
                .isEqualTo(otherUser)
                .hasSameHashCodeAs(otherUser);
    }

    @DisplayName("비밀번호 일치 확인 테스트")
    @Test
    void checkPassword() {
        // given
        final String correctPassword = user.getPassword();
        final String incorrectPassword = correctPassword + "1";

        // when
        // then
        assertThat(user.checkPassword(correctPassword)).isTrue();
        assertThat(user.checkPassword(incorrectPassword)).isFalse();
    }

    @DisplayName("이메일 일치 확인 테스트")
    @Test
    void hasSameEmail() {
        // given
        final String correctEmail = user.getEmail();
        final String incorrectEmail = "1" + correctEmail;

        // when
        // then
        assertThat(user.hasSameEmail(correctEmail)).isTrue();
        assertThat(user.hasSameEmail(incorrectEmail)).isFalse();
    }
}
