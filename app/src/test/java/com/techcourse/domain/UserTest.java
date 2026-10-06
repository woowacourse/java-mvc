package com.techcourse.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("사용자")
class UserTest {

    @Nested
    @DisplayName("문자열 표현")
    class StringRepresentation {

        @Test
        @DisplayName("문자열에 비밀번호를 노출하지 않는다")
        void doesNotExposePassword() {
            // given
            final var password = "private-password-for-test";
            final var user = new User(1, "learner", password, "learner@example.com");

            // when
            final var result = user.toString();

            // then
            assertThat(result).doesNotContain(password);
        }
    }

    @Nested
    @DisplayName("비밀번호 확인")
    class PasswordVerification {

        @Test
        @DisplayName("비밀번호가 일치하면 인증에 성공한다")
        void acceptsMatchingPassword() {
            // given
            final var user = new User(1, "learner", "correct-password", "learner@example.com");

            // when
            final var matches = user.checkPassword("correct-password");

            // then
            assertThat(matches).isTrue();
        }

        @Test
        @DisplayName("비밀번호가 다르면 인증에 실패한다")
        void rejectsDifferentPassword() {
            // given
            final var user = new User(1, "learner", "correct-password", "learner@example.com");

            // when
            final var matches = user.checkPassword("wrong-password");

            // then
            assertThat(matches).isFalse();
        }
    }
}
