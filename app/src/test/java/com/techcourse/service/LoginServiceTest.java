package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.exception.UnAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("LoginService 비즈니스 로직 테스트")
class LoginServiceTest {

    private static final String EXISTING_ACCOUNT = "gugu";
    private static final String CORRECT_PASSWORD = "password";

    private LoginService loginService;

    @BeforeEach
    void setUp() {
        loginService = new LoginService();
    }

    @DisplayName("로그인 성공")
    @Test
    void login() {
        // given

        // when
        final User loggedInUser = loginService.login(EXISTING_ACCOUNT, CORRECT_PASSWORD);

        // then
        assertThat(loggedInUser.getAccount()).isEqualTo(EXISTING_ACCOUNT);
        assertThat(loggedInUser.getPassword()).isEqualTo(CORRECT_PASSWORD);
    }

    @DisplayName("로그인 실패 - 존재하지 않는 account")
    @Test
    void loginFailureWhenAccountNotExists() {
        // given
        // when
        // then
        assertThatThrownBy(() -> loginService.login(EXISTING_ACCOUNT + 1, CORRECT_PASSWORD))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("로그인 실패 - 존재하는 account, 일치하지 않는 password")
    @Test
    void loginFailureWhenPasswordIsNotCorrect() {
        // given
        // when
        // then
        assertThatThrownBy(() -> loginService.login(EXISTING_ACCOUNT, CORRECT_PASSWORD + 1))
                .isInstanceOf(UnAuthorizedException.class);
    }
}