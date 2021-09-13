package com.techcourse.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.techcourse.controller.request.LoginRequest;
import com.techcourse.domain.User;
import com.techcourse.exception.UnauthorizedException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LoginService는")
class LoginServiceTest {

    private LoginService loginService;
    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setUp() {
        Map<String, User> database = new HashMap<>();
        userRepository = new InMemoryUserRepository(database, new AtomicLong(1));
        loginService = new LoginService(userRepository);
    }

    @DisplayName("login 시도시")
    @Nested
    class login {

        @DisplayName("password가 일치하지 않는다면 예외가 발생한다.")
        @Test
        void unmatchedPassword() {
            // given
            String account = "account";
            String password = "password";
            HttpSession httpSession = mock(HttpSession.class);
            userRepository.save(new User(account, password, "email"));

            LoginRequest loginRequest = new LoginRequest(account, password + "123", httpSession);

            // when, then
            assertThatThrownBy(() -> loginService.login(loginRequest))
                .isExactlyInstanceOf(UnauthorizedException.class);
        }

        @DisplayName("일치하는 account가 없다면 예외가 발생한다.")
        @Test
        void notFoundAccount() {
            // given
            HttpSession httpSession = mock(HttpSession.class);
            LoginRequest loginRequest = new LoginRequest("account", "password", httpSession);

            // when, then
            assertThatThrownBy(() -> loginService.login(loginRequest))
                .isExactlyInstanceOf(UnauthorizedException.class);
        }
    }
}