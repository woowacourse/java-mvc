package com.techcourse.controller.regacy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LoginViewControllerTest extends ControllerTest {

    LoginViewController loginViewController = new LoginViewController();

    @ParameterizedTest
    @ValueSource(strings = {"POST", "DELETE", "PUT", "PATCH"})
    void notGetSendRedirect(String method) {
        // given
        given(httpServletRequest.getMethod())
            .willReturn(method);

        String expect = "/404.jsp";

        // when
        String actual = loginViewController.execute(httpServletRequest, null);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void loggedInUserSendRedirect() {
        // given
        User mockUser = mock(User.class);
        given(UserSession.getUserFrom(any()))
            .willReturn(Optional.of(mockUser));
        given(httpServletRequest.getMethod())
            .willReturn("GET");

        String expect = "redirect:/index.jsp";

        // when
        String actual = loginViewController.execute(httpServletRequest, null);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void loginUserSendRedirect() {
        // given
        given(UserSession.getUserFrom(any()))
            .willReturn(Optional.empty());
        given(httpServletRequest.getMethod())
            .willReturn("GET");

        String expect = "/login.jsp";

        // when
        String actual = loginViewController.execute(httpServletRequest, null);

        // then
        assertThat(actual).isEqualTo(expect);
    }
}
