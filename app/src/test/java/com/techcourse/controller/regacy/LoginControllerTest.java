package com.techcourse.controller.regacy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

class LoginControllerTest {

    @Mock
    HttpServletRequest httpServletRequest;

    MockedStatic<UserSession> mockUserSession;
    MockedStatic<InMemoryUserRepository> mockUserRepository;
    LoginController loginController = new LoginController();

    @BeforeEach
    void setting() {
        MockitoAnnotations.openMocks(this);
        mockUserSession = mockStatic(UserSession.class);
        mockUserRepository = mockStatic(InMemoryUserRepository.class);
    }

    @AfterEach
    void deleteStaticMock() {
        mockUserSession.close();
        mockUserRepository.close();
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "DELETE", "PUT", "PATCH"})
    void returnRedirectWhenMethodNotPost(String method) {
        // given
        given(httpServletRequest.getMethod())
            .willReturn(method);
        String expect = "redirect:/login.jsp";

        // when
        String actual = loginController.execute(httpServletRequest, null);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void loginUserSendRedirect() {
        // given
        given(UserSession.isLoggedIn(any()))
            .willReturn(true);
        given(httpServletRequest.getMethod())
            .willReturn("POST");

        String expect = "redirect:/index.jsp";

        // when
        String actual = loginController.execute(httpServletRequest, null);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void existUserRedirectIndex() {
        // given
        given(UserSession.isLoggedIn(any()))
            .willReturn(false);
        given(httpServletRequest.getMethod())
            .willReturn("POST");

        HttpSession mockSession = mock(HttpSession.class);
        given(httpServletRequest.getSession())
            .willReturn(mockSession);

        User mockUser = mock(User.class);
        given(mockUser.checkPassword(any()))
            .willReturn(true);
        given(InMemoryUserRepository.findByAccount(any()))
            .willReturn(Optional.of(mockUser));

        String expect = "redirect:/index.jsp";

        // when
        String actual = loginController.execute(httpServletRequest, null);

        // then
        assertAll(
            () -> assertThat(actual).isEqualTo(expect),
            () -> verify(mockSession, times(1)).setAttribute(any(), any())
        );
    }

    @Test
    void invalidPassword40Redirect() {
        // given
        given(UserSession.isLoggedIn(any()))
            .willReturn(false);
        given(httpServletRequest.getMethod())
            .willReturn("POST");

        HttpSession mockSession = mock(HttpSession.class);
        given(httpServletRequest.getSession())
            .willReturn(mockSession);

        User mockUser = mock(User.class);
        given(mockUser.checkPassword(any()))
            .willReturn(false);
        given(InMemoryUserRepository.findByAccount(any()))
            .willReturn(Optional.of(mockUser));

        String expect = "redirect:/401.jsp";

        // when
        String actual = loginController.execute(httpServletRequest, null);

        // then
        assertThat(actual).isEqualTo(expect);

    }
}
