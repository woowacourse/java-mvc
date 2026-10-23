package com.techcourse.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UserTest {

    @Test
    @DisplayName("회원 문자열은 계정을 포함하되 비밀번호를 노출하지 않는다")
    void exclude_password_from_string_representation() {
        final var account = UUID.randomUUID().toString();
        final var password = "secret-" + UUID.randomUUID();
        final var user = new User(1, account, password, "user@example.com");

        assertThat(user.toString())
                .contains(account)
                .doesNotContain(password, "password=");
    }
}
