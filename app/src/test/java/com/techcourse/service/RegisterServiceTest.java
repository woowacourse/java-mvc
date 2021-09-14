package com.techcourse.service;

import com.techcourse.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    private static final RegisterService registerService = new RegisterService();

    @DisplayName("회원가입을 한다. - 성공")
    @Test
    void join() {
        // given
        String account = "joanne";
        String password = "1234";
        String email = "joanne@woowahan.com";

        // when
        final User user = registerService.join(account, password, email);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getAccount()).isEqualTo(account);
    }
}
