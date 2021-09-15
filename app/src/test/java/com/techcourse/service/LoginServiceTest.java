package com.techcourse.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.techcourse.domain.User;
import com.techcourse.exception.LoginFailedException;
import com.techcourse.exception.UserNotFoundException;
import com.techcourse.service.dto.LoginDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginServiceTest {

    private static final LoginService loginService = new LoginService();

    @DisplayName("로그인을 한다. - 성공")
    @Test
    void login() {
        // given
        String account = "gugu";
        String password = "password";

        // when
        final User user = loginService.login(LoginDto.of(account, password));

        // then
        assertThat(user).isNotNull();
        assertThat(user.getAccount()).isEqualTo(account);
    }

    @DisplayName("로그인을 한다. - 실패, 존재하지 않는 회원")
    @Test
    void loginFailedWhenNotFound() {
        // given
        String account = "존재하지 않는 회원";
        String password = "password";

        // when - then
        assertThatThrownBy(() -> loginService.login(LoginDto.of(account, password)))
            .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("로그인을 한다. - 실패, 비밀번호가 일치하지 않음.")
    @Test
    void loginFailedWhenLoginFailed() {
        // given
        String account = "gugu";
        String password = "melong";

        // when - then
        assertThatThrownBy(() -> loginService.login(LoginDto.of(account, password)))
            .isInstanceOf(LoginFailedException.class);
    }
}
